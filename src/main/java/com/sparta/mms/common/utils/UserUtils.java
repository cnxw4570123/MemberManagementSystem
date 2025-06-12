package com.sparta.mms.common.utils;


import com.sparta.mms.common.exception.InvalidTokenException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "액세스 토큰 발급 시점 검증")
public class UserUtils {

    private static final ConcurrentHashMap<Long, LocalDateTime> isUserModified = new ConcurrentHashMap<>();

    /**
     * 유저의 권한 변경 또는 탈퇴 시 해당 시간 기록 <br>
     * @Param userId <br>
     * - 관리자가 유저 권한 변경 하는 경우 PathVariable로 전달받은 값 <br>
     * - 유저가 탈퇴한 경우 유저 본인의 식별자 값  <br>
     * 이후 인가 과정에서는 먼저 userId로 변경 내역이 있는지 확인
     */
    public static void markUserModified(long userId) {
        LocalDateTime now = LocalDateTime.now();

        // 권한 변경이나 삭제 시간 매핑
        isUserModified.put(userId, now);
    }

    /**
     * userId로 조회 했을 때 <br>
     * - null인 경우 -> 변경된 적이 없음, 정상적으로 인가 <br>
     * - 액세스 토큰 발급 시점 >= 유저 변경 시점 -> 변경 내용 반영되었기 때문에 정상 처리 <br>
     * - 액세스 토큰 발급 시점 < 유저 변경 시점 -> 변경 이전에 발급한 토큰이기 때문에 반영이 되어있지 않음, 차단
     * @param userId : 검색할 유저 id
     */
    public static void checkAccessTokenBlocked(long userId, LocalDateTime issuedAt) {
        LocalDateTime modifiedAt = isUserModified.getOrDefault(userId, null);
        //
        if (modifiedAt != null && issuedAt.isBefore(modifiedAt)) {
            log.error("[ERROR] 권한 변경 / 탈퇴 이전에 발급된 토큰입니다.");
            throw new InvalidTokenException();
        }

        if (modifiedAt != null) {
            deleteIfExpired(userId, modifiedAt);
        }
    }

    /**
     * 추후 scheduler나 Redis TTL 사용해서 이미 만료기간이 지난 경우 제거
     */
    public static void deleteIfExpired(long userId, LocalDateTime modifiedAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.ofMillis(1209600 * 1_000L);
        LocalDateTime modifiedAtPlusAccessTokenExp = modifiedAt.plus(duration);

        // 현재 시점이 변경 시점 + 만료 기간 보다 이전일 경우 남겨둬야 함.
        if (now.isBefore(modifiedAtPlusAccessTokenExp)) {
            return;
        }

        log.info("now = {}, 변경 시점 + 만료기간 = {}", now, modifiedAtPlusAccessTokenExp);
        // 현재 시점이 변경 시점 + 만료 기간 보다 이후라면 아무리 빨리 발급해도 변경 시점 이후에 발급한 것.
        isUserModified.remove(userId);

    }
}
