package com.kh.delivery_project.util;

public interface Codes {

    // permission 관련
    int PREMISSIONS = 1000;
    // permission 관련 끝

    // intent 관련
    // 회원가입
    int ACTIVITY_DELIVER_REGIST = 10000;
    int REGIST_DLVR_IMG = 10001;
    int REGIST_DLVR_IDCARD = 10002;
    int REGIST_DLVR_BIRTH = 10003;
    int REGIST_DLVR_ADDR = 10004;

    // 회원정보 수정
    int MOD_DLVR_IMG = 10101;
    int MOD_DLVR_ADDR = 10102;

    // 배달 관련
    int PICK_ORDER = 10201;

    // 타임라인 관련
    int WRITE_TIME_IMG = 10301;
    int MOD_TIME_IMG = 10302;

    // timeline 관련
    int ALL_TIMELINE = 410;
    int NOTICE_TIMELINE = 411;
    int REVIEW_TIMELINE = 412;
    int FREE_TIMELINE = 413;
    // 회원가입 관련 끝

    // s3 관련
    // 이미지 앞주소
    String IMAGE_ADDRESS = "https://delivery-img2.s3.ap-northeast-2.amazonaws.com/";

    // 버킷 내 폴더명
    String DLVR_IMG = "Dlvr_Img/";
    String DLVR_IDCARD = "Dlvr_IDCard/";
    String TIMELINE_IMG = "Timeline_Img/";
    // s3 관련 끝
}
