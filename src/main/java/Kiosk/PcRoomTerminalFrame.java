package Kiosk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class PcRoomTerminalFrame extends JFrame {

    enum KioskTheme {
        DARK("다크",
                new Color(60, 60, 60), new Color(40, 40, 40),
                new Color(25, 25, 25), new Color(240, 240, 240),
                new Color(34, 34, 34), new Color(24, 24, 24),
                new Color(200, 200, 200), new Color(160, 160, 160),
                new Color(45, 45, 45), new Color(75, 75, 75)),

        WHITE("화이트",
                new Color(210, 210, 212), new Color(190, 190, 192),
                new Color(255, 255, 255), new Color(42, 42, 44),
                new Color(244, 244, 246), new Color(230, 230, 235),
                new Color(60, 60, 62), new Color(100, 100, 105),
                new Color(238, 238, 240), new Color(200, 200, 205)),

        BLUE_PURPLE("블루퍼플",
                new Color(116, 143, 252), new Color(186, 150, 252), // 밝은 파스텔 블루, 연보라 테두리
                new Color(243, 240, 253), new Color(59, 23, 133), // 터미널 배경(연한 톤), 터미널 글자색(진보라)
                new Color(195, 218, 254), new Color(232, 218, 254), // 밝은 하늘색, 연보라 그라데이션 배경
                new Color(95, 61, 196), new Color(150, 110, 250), // 타이틀, 프롬프트
                new Color(255, 255, 255), new Color(186, 150, 252));

        final String name;
        final Color color1;
        final Color color2;
        final Color termBg;
        final Color termFg;
        final Color bgGradStart;
        final Color bgGradEnd;
        final Color titleColor;
        final Color promptColor;
        final Color inputBg;
        final Color inputBorderColor;

        KioskTheme(String name, Color color1, Color color2, Color termBg, Color termFg, Color bgGradStart,
                Color bgGradEnd, Color titleColor, Color promptColor, Color inputBg, Color inputBorderColor) {
            this.name = name;
            this.color1 = color1;
            this.color2 = color2;
            this.termBg = termBg;
            this.termFg = termFg;
            this.bgGradStart = bgGradStart;
            this.bgGradEnd = bgGradEnd;
            this.titleColor = titleColor;
            this.promptColor = promptColor;
            this.inputBg = inputBg;
            this.inputBorderColor = inputBorderColor;
        }
    }

    // 슬라이딩 애니메이션을 위한 투 패널 컨테이너
    private JPanel terminalContainer;
    private JTextArea activeTerminal;
    private JTextArea nextTerminal;
    private JScrollPane activeScroll;
    private JScrollPane nextScroll;

    private JTextField inputField;
    private JLabel titleLabel;
    private JLabel promptLabel;
    private PipedOutputStream pipedOut;
    private PipedInputStream pipedIn;

    private KioskTheme currentTheme = KioskTheme.DARK;

    // 버퍼 및 상태 관리
    private final StringBuilder currentScreenBuffer = new StringBuilder();
    private final StringBuilder incomingBuffer = new StringBuilder();
    private boolean awaitingResponse = false;
    private Timer responseTimer;

    // 애니메이션 제어
    private Timer animationTimer;
    private long animationStart;
    private static final int ANIMATION_DURATION = 350; // 밀리초 (부드러운 슬라이딩 속도)

    private int mouseX;
    private int mouseY;

    public PcRoomTerminalFrame() {
        setUndecorated(true);
        setTitle("macOS Terminal - KIOSK");
        setSize(640, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint bgGrad = new GradientPaint(0, 0, currentTheme.bgGradStart, 0, getHeight(),
                        currentTheme.bgGradEnd);
                g2d.setPaint(bgGrad);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2d.setStroke(new BasicStroke(2.0f));
                GradientPaint borderGrad = new GradientPaint(0, 0, currentTheme.color1, getWidth(), getHeight(),
                        currentTheme.color2);
                g2d.setPaint(borderGrad);
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 16, 16);
            }
        };
        mainPanel.setBorder(new EmptyBorder(12, 16, 12, 16));
        setContentPane(mainPanel);

        // 1. macOS 타이틀바
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setPreferredSize(new Dimension(980, 35));

        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });

        JPanel trafficLightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        trafficLightPanel.setOpaque(false);

        JButton closeBtn = createMacButton(new Color(255, 95, 86), new Color(220, 70, 65), () -> System.exit(0));
        JButton minBtn = createMacButton(new Color(255, 189, 46), new Color(220, 160, 35),
                () -> setExtendedState(JFrame.ICONIFIED));
        JButton zoomBtn = createMacButton(new Color(39, 201, 63), new Color(30, 170, 50), null);

        trafficLightPanel.add(closeBtn);
        trafficLightPanel.add(minBtn);
        trafficLightPanel.add(zoomBtn);
        titleBar.add(trafficLightPanel, BorderLayout.WEST);

        titleLabel = new JLabel("Terminal — bash — 80×24", SwingConstants.CENTER);
        titleLabel.setFont(new Font("San Francisco", Font.BOLD, 12));
        titleLabel.setForeground(currentTheme.titleColor);
        titleBar.add(titleLabel, BorderLayout.CENTER);

        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(80, 30));
        titleBar.add(spacer, BorderLayout.EAST);

        mainPanel.add(titleBar, BorderLayout.NORTH);

        // 2. 중앙 터미널 컨테이너 (슬라이딩 애니메이션 뷰포트)
        terminalContainer = new JPanel(null); // 절대 좌표 레이아웃
        terminalContainer.setOpaque(false);

        // 터미널 1 (Active)
        activeTerminal = createTerminalArea();
        activeScroll = createTerminalScrollPane(activeTerminal);

        // 터미널 2 (Next)
        nextTerminal = createTerminalArea();
        nextScroll = createTerminalScrollPane(nextTerminal);

        terminalContainer.add(activeScroll);
        terminalContainer.add(nextScroll);

        // 레이아웃 배치 이벤트 처리 (크기 변화 대응)
        terminalContainer.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (animationTimer == null || !animationTimer.isRunning()) {
                    activeScroll.setBounds(0, 0, terminalContainer.getWidth(), terminalContainer.getHeight());
                    nextScroll.setBounds(terminalContainer.getWidth(), 0, terminalContainer.getWidth(),
                            terminalContainer.getHeight());
                }
            }
        });

        mainPanel.add(terminalContainer, BorderLayout.CENTER);

        // 3. 우측 테마 설정 패널
        JPanel rightThemePanel = new JPanel();
        rightThemePanel.setOpaque(false);
        rightThemePanel.setLayout(new BoxLayout(rightThemePanel, BoxLayout.Y_AXIS));
        rightThemePanel.setBorder(new EmptyBorder(0, 16, 0, 0));

        JLabel themeTitle = new JLabel("THEME SELECT");
        themeTitle.setFont(new Font("San Francisco", Font.BOLD, 11));
        themeTitle.setForeground(currentTheme.titleColor);
        themeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightThemePanel.add(themeTitle);
        rightThemePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        for (KioskTheme t : KioskTheme.values()) {
            JButton themeBtn = new JButton(t.name) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isPressed()) {
                        g2d.setColor(new Color(150, 150, 150, 100));
                    } else if (getModel().isRollover()) {
                        g2d.setColor(new Color(200, 200, 200, 40));
                    } else {
                        g2d.setColor(new Color(255, 255, 255, 15));
                    }
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    super.paintComponent(g);
                }
            };
            themeBtn.setFont(new Font("맑은 고딕", Font.BOLD, 12));
            themeBtn.setForeground(currentTheme.titleColor);
            themeBtn.setContentAreaFilled(false);
            themeBtn.setFocusPainted(false);
            themeBtn.setMaximumSize(new Dimension(110, 35));
            themeBtn.setPreferredSize(new Dimension(110, 35));
            themeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            themeBtn.setBorder(BorderFactory.createLineBorder(t.color1, 1));

            themeBtn.addActionListener(e -> applyTheme(t));

            rightThemePanel.add(themeBtn);
            rightThemePanel.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        mainPanel.add(rightThemePanel, BorderLayout.EAST);

        // 4. 하단 입력 패널
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(new EmptyBorder(12, 0, 4, 0));

        promptLabel = new JLabel("bash-3.2$ ");
        promptLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        promptLabel.setForeground(currentTheme.promptColor);
        inputPanel.add(promptLabel, BorderLayout.WEST);

        inputField = new JTextField();
        inputField.setBackground(currentTheme.inputBg);
        inputField.setForeground(currentTheme.termFg);
        inputField.setCaretColor(currentTheme.termFg);
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(currentTheme.inputBorderColor, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        inputPanel.add(inputField, BorderLayout.CENTER);

        inputField.addActionListener(e -> handleInputSend());

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // 입출력 감지 타이머 설정 (50ms 동안 추가 출력이 없으면 화면 업데이트 완료로 판단)
        responseTimer = new Timer(50, e -> triggerScreenTransition());
        responseTimer.setRepeats(false);

        setupStreams();
    }

    private JTextArea createTerminalArea() {
        JTextArea area = new JTextArea();
        area.setBackground(currentTheme.termBg);
        area.setForeground(currentTheme.termFg);
        area.setCaretColor(currentTheme.termFg);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(12, 12, 12, 12));
        return area;
    }

    private JScrollPane createTerminalScrollPane(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100, 50), 1));
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scroll.getVerticalScrollBar().setBackground(currentTheme.termBg);
        return scroll;
    }

    private JButton createMacButton(Color baseColor, Color hoverColor, Runnable action) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover() && action != null) {
                    g2d.setColor(hoverColor);
                } else {
                    g2d.setColor(baseColor);
                }
                g2d.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        btn.setPreferredSize(new Dimension(13, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        if (action != null) {
            btn.addActionListener(e -> action.run());
        }
        return btn;
    }

    private void applyTheme(KioskTheme theme) {
        this.currentTheme = theme;

        titleLabel.setForeground(theme.titleColor);
        promptLabel.setForeground(theme.promptColor);

        activeTerminal.setBackground(theme.termBg);
        activeTerminal.setForeground(theme.termFg);
        activeTerminal.setCaretColor(theme.termFg);
        activeScroll.getVerticalScrollBar().setBackground(theme.termBg);

        nextTerminal.setBackground(theme.termBg);
        nextTerminal.setForeground(theme.termFg);
        nextTerminal.setCaretColor(theme.termFg);
        nextScroll.getVerticalScrollBar().setBackground(theme.termBg);

        inputField.setBackground(theme.inputBg);
        inputField.setForeground(theme.termFg);
        inputField.setCaretColor(theme.termFg);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.inputBorderColor, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        repaint();
    }

    private void setupStreams() {
        try {
            pipedIn = new PipedInputStream();
            pipedOut = new PipedOutputStream(pipedIn);
            System.setIn(pipedIn);

            OutputStream outRedirect = new OutputStream() {
                private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                @Override
                public void write(int b) throws IOException {
                    buffer.write(b);
                    if (b == '\n') {
                        flushBuffer();
                    }
                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    buffer.write(b, off, len);
                    flushBuffer();
                }

                private void flushBuffer() {
                    if (buffer.size() > 0) {
                        String text = buffer.toString(StandardCharsets.UTF_8);
                        buffer.reset();
                        SwingUtilities.invokeLater(() -> {
                            if (awaitingResponse) {
                                // 사용자가 입력을 넣은 후 들어오는 반응 데이터는 임시 버퍼에 누적
                                incomingBuffer.append(text);
                                responseTimer.restart(); // 타이머 리셋
                            } else {
                                // 초기 부팅 단계나 일반 로그는 실시간 출력
                                activeTerminal.append(text);
                                activeTerminal.setCaretPosition(activeTerminal.getDocument().getLength());
                                currentScreenBuffer.append(text);
                            }
                        });
                    }
                }
            };

            PrintStream ps = new PrintStream(outRedirect, true, "UTF-8");
            System.setOut(ps);
            System.setErr(ps);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleInputSend() {
        String text = inputField.getText();
        inputField.setText("");

        // 사용자가 보낸 입력 에코 출력
        SwingUtilities.invokeLater(() -> {
            activeTerminal.append(text + "\n");
            activeTerminal.setCaretPosition(activeTerminal.getDocument().getLength());
            currentScreenBuffer.append(text).append("\n");

            // 사용자 입력 완료 시점부터 백엔드 응답 대기 상태로 진입
            awaitingResponse = true;
            incomingBuffer.setLength(0); // 수신 버퍼 클리어
        });

        try {
            pipedOut.write((text + "\n").getBytes(StandardCharsets.UTF_8));
            pipedOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 좌우로 부드럽게 화면을 넘기는 트랜지션 슬라이딩 애니메이션 실행
    private void triggerScreenTransition() {
        awaitingResponse = false;
        if (incomingBuffer.length() == 0)
            return;

        String nextText = incomingBuffer.toString();

        // 슬라이드 전환 조건 검사: 구분선(===, ---)이나 완료/알림 메시지가 있을 때만 전체 화면을 전환
        boolean shouldSlide = nextText.contains("===") ||
                nextText.contains("---") ||
                nextText.contains("환영합니다") ||
                nextText.contains("로그아웃") ||
                nextText.contains("[알림]") ||
                nextText.contains("등록되었습니다") ||
                nextText.contains("제거되었습니다") ||
                nextText.contains("수정되었습니다");

        if (shouldSlide) {
            // 새 화면에 표시될 텍스트 준비
            nextTerminal.setText(nextText);
            nextTerminal.setCaretPosition(0);

            // 애니메이션 시작 시점 위치 잡기
            int width = terminalContainer.getWidth();
            int height = terminalContainer.getHeight();

            activeScroll.setBounds(0, 0, width, height);
            nextScroll.setBounds(width, 0, width, height); // 대기 패널은 우측 바깥에 위치
            nextScroll.setVisible(true);

            animationStart = System.currentTimeMillis();

            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }

            animationTimer = new Timer(15, event -> {
                long elapsed = System.currentTimeMillis() - animationStart;
                double t = (double) elapsed / ANIMATION_DURATION;
                if (t >= 1.0) {
                    t = 1.0;
                    animationTimer.stop();

                    // 스왑 완료 처리
                    activeScroll.setBounds(width, 0, width, height);
                    nextScroll.setBounds(0, 0, width, height);

                    // 변수 스왑
                    JTextArea tempArea = activeTerminal;
                    activeTerminal = nextTerminal;
                    nextTerminal = tempArea;

                    JScrollPane tempScroll = activeScroll;
                    activeScroll = nextScroll;
                    nextScroll = tempScroll;

                    // 다음 화면을 위한 스크린 버퍼 갱신
                    currentScreenBuffer.setLength(0);
                    currentScreenBuffer.append(nextText);
                } else {
                    // 부드러운 감속 효과 적용 (ease-out-cubic)
                    double ease = 1.0 - Math.pow(1.0 - t, 3.0);
                    int offset = (int) (width * ease);

                    activeScroll.setBounds(-offset, 0, width, height);
                    nextScroll.setBounds(width - offset, 0, width, height);
                }
            });

            animationTimer.start();
        } else {
            // 슬라이드 하지 않고 현재 화면에 그대로 덧붙임 (아이디/비밀번호 등 순차 입력을 한 화면에서 처리)
            activeTerminal.append(nextText);
            activeTerminal.setCaretPosition(activeTerminal.getDocument().getLength());
            currentScreenBuffer.append(nextText);
        }
    }

    public void startApplication(Runnable appMain) {
        SwingUtilities.invokeLater(() -> setVisible(true));

        Thread appThread = new Thread(appMain, "Kiosk-Core-Thread");
        appThread.setDaemon(true);
        appThread.start();
    }
}
