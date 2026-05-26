package Kiosk.dao;

import Kiosk.domain.Member;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonMemberDaoImpl implements MemberDao {
    private static final String FILE_PATH = "src/main/resources/members.json";
    private Map<String, Member> db = new HashMap<>();
    private final Gson gson;

    public JsonMemberDaoImpl() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                        LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setPrettyPrinting()
                .create();
        loadMembers();
    }

    private void loadMembers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Member>>() {}.getType();
            List<Member> members = gson.fromJson(reader, listType);
            if (members != null) {
                for (Member m : members) {
                    db.put(m.getMemberId(), m);
                }
            }
        } catch (IOException e) {
            System.err.println("JSON 파일을 읽어오는 중 오류 발생: " + e.getMessage());
        }
    }

    private void saveMembers() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(db.values(), writer);
        } catch (IOException e) {
            System.err.println("JSON 파일에 저장하는 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public void insertMember(Member member) {
        db.put(member.getMemberId(), member);
        saveMembers();
        System.out.println("[JSON DB] 회원 등록 완료: " + member.getMemberId());
    }

    @Override
    public Member getMemberById(String memberId) {
        return db.get(memberId);
    }

    @Override
    public void updateLastLoginDate(String memberId) {
        Member member = db.get(memberId);
        if (member != null) {
            member.setLastLoginDate(LocalDateTime.now());
            saveMembers();
            System.out.println("[JSON DB] 접속 일시 업데이트 완료: " + memberId);
        }
    }
}
