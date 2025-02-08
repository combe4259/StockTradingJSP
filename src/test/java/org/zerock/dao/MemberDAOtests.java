package org.zerock.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.stocktrading.dao.MemberDAO;
import org.zerock.stocktrading.domain.MemberVO;
import org.zerock.stocktrading.dto.MemberDTO;
import org.zerock.stocktrading.service.MemberSerivce;

public class MemberDAOtests {

    private MemberDAO memberDAO;
    private MemberSerivce memberSerivce;

    @BeforeEach
    public void ready(){
        memberDAO = new MemberDAO();
        memberSerivce= MemberSerivce.INSTANCE;
    }

    @Test
    public void testSelectUserID() throws Exception{

        System.out.println(memberDAO.selectUserID("1"));
    }

    @Test
    public void getwithpasswordtests() throws Exception{

        System.out.println(memberDAO.getWithPassword("1","abcd"));


    }

    @Test
    public void registertests() throws Exception {
        // 현재 시간을 이용해 유니크한 ID 생성
        String uniqueId = "test_" + System.currentTimeMillis();

        MemberDTO memberDTO = MemberDTO.builder()
                .user_id(uniqueId)
                .password("aaaa")
                .username("Jun")
                .build();

        memberSerivce.register(memberDTO);
    }
}
