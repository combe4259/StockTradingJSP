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
    public void registertests() throws Exception{
        MemberDTO memberDTO = MemberDTO.builder()
                .user_id("1111")
                .password("aaaa")
                .username("Jun")
                .build();

        memberSerivce.register(memberDTO);
    }
}
