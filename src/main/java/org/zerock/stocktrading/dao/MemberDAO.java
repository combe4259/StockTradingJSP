package org.zerock.stocktrading.dao;

import lombok.Cleanup;
import org.checkerframework.checker.units.qual.C;
import org.zerock.stocktrading.domain.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {

    //member ID 하나 고르기
    public MemberVO selectUserID(String user_id) throws Exception{
        String query = "select member_id, user_id, username from tbl_member where user_id = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user_id);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return MemberVO.builder()
                    .member_id(resultSet.getInt("member_id"))
                    .user_id(resultSet.getString("user_id"))
                    .username(resultSet.getString("username"))
                    .build();
        } else {
            System.out.println("사용자를 찾을 수 없음: " + user_id);
            return null;
        }
    }
    //업데이트하기


}
