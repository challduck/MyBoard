package dev.challduck.portfolio.service;

import dev.challduck.portfolio.domain.Member;
import dev.challduck.portfolio.domain.MemberLoginLog;
import dev.challduck.portfolio.repository.MemberLoginLogRepository;
import dev.challduck.portfolio.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberLoginLogService {
    private final MemberRepository memberRepository;
    private final MemberLoginLogRepository memberLoginLogRepository;

    @Transactional
    public void memberLoginLogSave(String email, HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info(">>>> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info(">>>> WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info(">>>> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info(">>>> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        log.info(">>>> Result : IP Address : "+ip);

        LocalDateTime loginDate = LocalDateTime.now();

        Member member = memberRepository.findByEmail(email).get();

        MemberLoginLog memberLoginLog = MemberLoginLog.builder()
                .member(member)
                .loginDate(loginDate)
                .loginIp(ip)
                .build();

        memberLoginLogRepository.save(memberLoginLog);
    }

    public MemberLoginLog getMemberLoginLog(Member member){
        List<MemberLoginLog> loginLogs = memberLoginLogRepository.findByMemberOrderByLoginDateDesc(member);
        if(loginLogs.size() >= 2){
            return loginLogs.get(1);
        }
        else {
            return createDummyLog(member);
        }
    }

    private MemberLoginLog createDummyLog(Member member) {
        return new MemberLoginLog(member, "마지막 로그인 기록이 없습니다.", LocalDateTime.now());
    }
}
