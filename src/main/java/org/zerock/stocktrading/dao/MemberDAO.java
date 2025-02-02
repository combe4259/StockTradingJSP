package org.zerock.stocktrading.dao;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.C;
import org.zerock.stocktrading.domain.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Log4j2
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
        }
//        else {
//            System.out.println("사용자를 찾을 수 없음: " + user_id);
//            return null;
//        }
        throw new RuntimeException("Invalid user_id or password");
    }
    //로그인
    public MemberVO getWithPassword(String user_id,String password) throws Exception{

        String query = "select user_id, password, username from tbl_member where user_id = ? and password = ?";


        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user_id);
        preparedStatement.setString(2, password);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();


        if(resultSet.next()){
            return MemberVO.builder()
                    .user_id(resultSet.getString(1))
                    .password(resultSet.getString(2))
                    .username(resultSet.getString(3))
                    .build();
            }
//        else{ //추후 해결
//            log.info("비밀번호와 일치하는 사용자 없음");
//            return null;
//
//        }
        throw new RuntimeException("Invalid user_id or password");
    }


}
