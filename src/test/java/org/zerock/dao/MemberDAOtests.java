package org.zerock.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.stocktrading.dao.MemberDAO;
import org.zerock.stocktrading.domain.MemberVO;

public class MemberDAOtests {

    private MemberDAO memberDAO;

    @BeforeEach
    public void ready(){
        memberDAO = new MemberDAO();
    }

    @Test
    public void testSelectUserID() throws Exception{

        System.out.println(memberDAO.selectUserID("1"));
    }
}
