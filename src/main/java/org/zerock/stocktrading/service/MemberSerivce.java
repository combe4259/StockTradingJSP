package org.zerock.stocktrading.service;

import org.modelmapper.ModelMapper;
import org.zerock.stocktrading.dao.MemberDAO;
import org.zerock.stocktrading.domain.MemberVO;
import org.zerock.stocktrading.dto.MemberDTO;
import org.zerock.stocktrading.util.MapperUtil;

public enum MemberSerivce {
    INSTANCE;

    private MemberDAO dao;
    private ModelMapper modelMapper;

    MemberSerivce(){
        dao = new MemberDAO();
        modelMapper= MapperUtil.INSTANCE.get();
    }

    public MemberDTO login(String user_id, String password) throws Exception{

        MemberVO vo = dao.getWithPassword(user_id, password);
        MemberDTO memberDTO = modelMapper.map(vo, MemberDTO.class);
        return memberDTO;

    }

    public void register(MemberDTO memberDTO) throws Exception{

        MemberVO memberVO = modelMapper.map(memberDTO, MemberVO.class);
        dao.insert(memberVO);

    }



}
