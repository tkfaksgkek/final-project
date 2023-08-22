package com.icia.sdsd.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.icia.sdsd.dao.MemberRepository;
import com.icia.sdsd.dao.NoticeRepository;
import com.icia.sdsd.dao.OPRepository;
import com.icia.sdsd.dto.MemberEntity;
import com.icia.sdsd.dto.NoticeDTO;
import com.icia.sdsd.dto.NoticeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AdminService {

    private ModelAndView mav;

    private final NoticeRepository nrepo;
    private final MemberRepository mrepo;
    private final OPRepository opreo;
    private final AmazonS3Client amazonS3Client;
    private String bucket="sdsdfile";


    public ModelAndView noticeWrite(NoticeDTO notice) {
        mav = new ModelAndView();
        System.out.println("서비스 " + notice);
        NoticeEntity entity = NoticeEntity.toEntity(notice);
        System.out.println("entity : "+entity);
        nrepo.save(entity);
        mav.setViewName("redirect:/notice");

        return mav;
    }

    public List<NoticeDTO> noticeList() {
        List<NoticeDTO> nList = new ArrayList<>();
        List<NoticeEntity> EntityList = nrepo.findAllByOrderBySnUpdateDateDesc();

        for(NoticeEntity entity : EntityList){
            nList.add(NoticeDTO.toDTO(entity));
        }
        System.out.println(nList);
        return nList;
    }

    // 공지사항 자세히 보기
    public ModelAndView snView(int snNum) {
        mav = new ModelAndView();

        NoticeEntity entity = nrepo.findById(snNum).get();
        NoticeDTO notice = NoticeDTO.toDTO(entity);

        mav.addObject("view",notice);
        mav.setViewName("/admin/noticeView");
        return mav;
    }

    public ModelAndView noticeModiForm(int snNum) {
        mav = new ModelAndView();

        mav = snView(snNum);
        mav.setViewName("/admin/noticeModiForm");
        return mav;
    }

    public ModelAndView noticeModify(NoticeDTO notice) {
        mav = new ModelAndView();
        NoticeEntity nEntity = NoticeEntity.toEntity(notice);
        System.out.println(nEntity);
        nrepo.save(nEntity);
        mav.setViewName("redirect:/snView/"+notice.getSnNum());
        return mav;
    }

    public String noticeDelete(int snNum) {
        mav.clear();
        NoticeDTO notiec=(NoticeDTO)snView(snNum).getModel().get("view");
        String contents=notiec.getSnContent();
        Pattern p = Pattern.compile("<img src=\"([^\"]+)");
        Matcher files = p.matcher(contents);
        while(files.find()) {
            System.out.println(files.group(1));
            delImage(files.group(1));
        }
        nrepo.deleteById(snNum);
        return "redirect:/notice";
    }

    // 게시판 이미지 삭제
    public String delImage(String delFile) {
        String key=delFile.substring(49);
        amazonS3Client.deleteObject(bucket, key);    //저장된 파일 삭제
        return key;
    }

    // 관리자 회원삭제
    public ModelAndView amDelete(String smId) {
        mav = new ModelAndView();
        Optional<MemberEntity> member = mrepo.findById(smId);
        String delFileName = member.get().getSmProfileName();

        try {
            mrepo.deleteById(smId);

            if(!delFileName.isEmpty()) {
                String key = delFileName.substring(49);
                System.out.println(key);
                amazonS3Client.deleteObject(bucket, key);    //저장된 파일 삭제
            }
            mav.setViewName("admin/AMList");
        } catch (Exception e){
            mav.setViewName("admin/AMList");
        }

        return mav;
    }

    // 관리자 사업자 삭제
    public ModelAndView aoDelete(String soNum) {
        mav = new ModelAndView();

        try {
            opreo.deleteById(soNum);
            mav.setViewName("admin/AOList");
        } catch (Exception e){
            mav.setViewName("admin/AOList");
        }
        return mav;
    }

}
