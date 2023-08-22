package com.icia.sdsd.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.JsonObject;
import com.icia.sdsd.dao.CommentRepository;
import com.icia.sdsd.dao.GBoardRepository;
import com.icia.sdsd.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GBService {

    private final GBoardRepository gbrepo;
    private ModelAndView mav;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final HttpSession session;

    private final CommentRepository cmtrepo;

    private final AmazonS3Client amazonS3Client;

    private final MemberService msvc;
    private String bucket="sdsdfile";

    public void GBWrite(GBoardDTO gBoardDTO) {
        System.out.println(gBoardDTO);
        // 작성자 구분
        String gbWriter = gBoardDTO.getGbWriter();
        if (gBoardDTO.getGbCode() == 1) {
            try {
                gbWriter = msvc.createNick();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        gBoardDTO.setGbWriter(gbWriter);

        GBoardEntity entity = GBoardEntity.toEntity(gBoardDTO);
        System.out.println(entity);
        gbrepo.save(entity);
    }


    public List<GBoardDTO> gBoardList() {
        List<GBoardDTO> gbList = new ArrayList<>();
        List<GBoardEntity> gbEntityList = gbrepo.findAllByOrderByGbNumDesc();

        for (GBoardEntity entity : gbEntityList) {
            gbList.add(GBoardDTO.toDTO(entity));
        }
        return gbList;
    }

    public ModelAndView gbView(int gbNum) {
        mav = new ModelAndView();

        String gbWriter = (String) session.getAttribute("loginId");

        if (gbWriter == null) {
            gbWriter = "Guest";
        }

        Optional<GBoardEntity> entity = gbrepo.findById(gbNum);

        Cookie[] cookies = request.getCookies();

        Cookie viewCookie = null;

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cookie_" + gbWriter.replace("@","_") + "_" + gbNum)) {
                    viewCookie = cookie;
                }
            }
        }

        if (viewCookie == null) {
            Cookie newCookie = new Cookie("cookie_" + gbWriter.replace("@","_") + "_" + gbNum, "cookie_" + gbWriter + "_" + gbNum);
            newCookie.setMaxAge(60 * 60 * 1);
            response.addCookie(newCookie);
            entity.get().setGbHit(entity.get().getGbHit()+1);
//            gbrepo.gbHit(gbNum);
            gbrepo.save(entity.get());

        }


        GBoardDTO gBoardDTO = GBoardDTO.toDTO(entity.get());

        mav.setViewName("/generalBoard/GBView");
        mav.addObject("view", gBoardDTO);

        return mav;
    }


    public ModelAndView gbModiForm(int gbNum) {
        mav = new ModelAndView();
        GBoardDTO gBoardDTO = (GBoardDTO) gbView(gbNum).getModel().get("view");

        mav.addObject("modify", gBoardDTO);
        mav.setViewName("/generalBoard/GBModify");

        return mav;
    }

    public void gbModify(GBoardDTO gBoardDTO) {

        System.out.println("확인 : " + gBoardDTO);
        mav = new ModelAndView();

        GBoardEntity entity = GBoardEntity.toEntity(gBoardDTO);
        gbrepo.save(entity);
    }


    public ModelAndView gbDelete(int gbNum) {
        System.out.println(gbNum);
        mav = new ModelAndView();
        GBoardDTO gBoardDTO = (GBoardDTO) gbView(gbNum).getModel().get("view");
        String contents=gBoardDTO.getGbContent();
        // 본문에서 정규식을 이용해서 img src 부분 추출하기
        Pattern p = Pattern.compile("<img src=\"([^\"]+)");
        Matcher files = p.matcher(contents);
        while(files.find()) {
            System.out.println(files.group(1));
            delImage(files.group(1));
        }


        List<CommentEntity> cmtList = cmtrepo.findAllByGcBNumOrderByGcNumDesc(gbNum);
        //List<CommentDTO> list = new ArrayList<>();
        for (CommentEntity cEntity : cmtList) {
            cmtrepo.delete(cEntity);
        }

        gbrepo.deleteById(gbNum);
//        session.removeAttribute("login");
        mav.setViewName("redirect:/GBList");

        return mav;
    }

    // 게시판 검색
    public List<GBoardDTO> GBSearch(SearchDTO search) {
        System.out.println("키워드"+search);

        List<GBoardDTO> GBList = new ArrayList<>();
        List<GBoardEntity> entityList= new ArrayList<>();
        if(search.getCategory().equals("gbTitle")) {
            entityList = gbrepo.findByGbTitleContainingIgnoreCaseOrderByGbNumDesc(search.getKeyword());
        }else if(search.getCategory().equals("gbContent")) {
            entityList = gbrepo.findByGbContentContainingIgnoreCaseOrderByGbNumDesc(search.getKeyword());
        }else if(search.getCategory().equals("gbWriter")) {
            entityList = gbrepo.findByGbWriterContainingIgnoreCaseOrderByGbNumDesc(search.getKeyword());
        }

        for (GBoardEntity entity : entityList){
            GBList.add(GBoardDTO.toDTO(entity));
        }
        System.out.println(GBList);
        return GBList;
    }

    // 자게 이미지 업로드
    public JsonObject uploadSummernoteImageFile(MultipartFile multipartFile) {

        JsonObject jsonObject = new JsonObject();                               //  url을 받을 json 객체 초기화
        String originalFileName = multipartFile.getOriginalFilename();    //오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));    //파일 확장자

        String savedFileName = UUID.randomUUID().toString().substring(0,8) + extension;    //저장될 파일 명

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String key = "upload/" + savedFileName;

        try {
            InputStream fileStream = multipartFile.getInputStream();
            amazonS3Client.putObject(new PutObjectRequest(bucket,key,fileStream,objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));;    //파일 저장
            jsonObject.addProperty("url", amazonS3Client.getUrl(bucket,key).toString());
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            amazonS3Client.deleteObject(bucket, key);    //저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }
        return jsonObject;
    }

    // 게시판 이미지 삭제
    public String delImage(String delFile) {
        String key=delFile.substring(49);
        amazonS3Client.deleteObject(bucket, key);    //저장된 파일 삭제
        return key;
    }

    // index영역 자유게시판 인기글( 조회수 )
    public List<GBoardDTO> indexGBlist() {
        List<GBoardDTO> gbList = new ArrayList<>();
        List<GBoardEntity> gBboardEntity = gbrepo.findAllByOrderByGbHitDesc();

        for(GBoardEntity entity : gBboardEntity){
            gbList.add(GBoardDTO.toDTO(entity));
        }

        return gbList;
    }

    //댓글 저장
    public List<CommentDTO> cWrite(CommentDTO cmt) {
        // 댓글 dto를 entity로 변환
        CommentEntity cmtentity = CommentEntity.toEntity(cmt);
        System.out.println(cmtentity);

        // JPA를 사용해서 댓글을 db에 입력
        cmtrepo.save(cmtentity);

        // JPA를 사용해서 해당게시글(gcBNum) 댓글 목록을 불러온다
        List<CommentEntity> cmtList = cmtrepo.findAllByGcBNumOrderByGcNumDesc(cmt.getGcBNum());
        System.out.println(cmtList);

        // Entity로 받아온 데이터를 DTO로 변환
        List<CommentDTO> list = new ArrayList<>();

        // 댓글의 갯수만큼 반복문 실행
        for(CommentEntity cEntity : cmtList){
            list.add(CommentDTO.toDTO(cEntity));
        }

        return list;
    }

    // 댓글목록
    public List<CommentDTO> cList(int gcBNum) {
        System.out.println("확인1 : " + gcBNum);

        List<CommentDTO> list = new ArrayList<>();

        // 예외가 발생하지 않으면 실행
        try{
            // Entity로 받아온 데이터를 DTO로 변환
            List<CommentEntity> cmtList = cmtrepo.findAllByGcBNumOrderByGcNumDesc(gcBNum);

            // 댓글의 갯수만큼 반복문 실행
            for(CommentEntity cEntity : cmtList){
                list.add(CommentDTO.toDTO(cEntity));
            }
        } catch (Exception e){
            // 예외가 발생했을 경우 처리
            e.printStackTrace();
        }
        return list;
    }

    // 댓글수정
    public List<CommentDTO> cModify(CommentDTO cmt) {
        Optional<CommentEntity> entity=cmtrepo.findById(cmt.getGcNum());
        entity.get().setGcContent(cmt.getGcContent());
        cmtrepo.save(entity.get());

        // JPA를 사용해서 해당게시글(gcBNum) 댓글 목록을 불러온다
        List<CommentEntity> cmtList = cmtrepo.findAllByGcBNumOrderByGcNumDesc(cmt.getGcBNum());
        System.out.println(cmtList);

        // Entity로 받아온 데이터를 DTO로 변환
        List<CommentDTO> list = new ArrayList<>();

        // 댓글의 갯수만큼 반복문 실행
        for (CommentEntity cEntity : cmtList) {
            list.add(CommentDTO.toDTO(cEntity));
        }

        return list;
    }

    // 댓글삭제
    public List<CommentDTO> cDelete(CommentDTO cmt) {
        mav = new ModelAndView();
        cmtrepo.deleteById(cmt.getGcNum());

        List<CommentEntity> cmtList = cmtrepo.findAllByGcBNumOrderByGcNumDesc(cmt.getGcBNum());
        List<CommentDTO> list = new ArrayList<>();
        for (CommentEntity cEntity : cmtList) {
            list.add(CommentDTO.toDTO(cEntity));
        }
        return list;
    }

    // 내가 쓴 글 조회
    public List<GBoardDTO> myGbList(String gbWriterId) {
        mav = new ModelAndView();

        List<GBoardDTO> GBList = new ArrayList<>();
        List<GBoardEntity> entityList= new ArrayList<>();
        entityList = gbrepo.findByGbWriterIdContainingIgnoreCaseOrderByGbNumDesc(gbWriterId);

        for (GBoardEntity entity : entityList){
            GBList.add(GBoardDTO.toDTO(entity));
        }
        System.out.println(GBList);
        return GBList;
    }

    public String countCmt(int gcbNum) {
        String result=null;
        List<CommentEntity> count=cmtrepo.findAllByGcBNumOrderByGcNumDesc(gcbNum);
        result=String.valueOf(count.size());

        return result;
    }
}
