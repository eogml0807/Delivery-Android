package com.kh.delivery_project.util;

public interface Codes {

    // permission 관련
    int PREMISSIONS = 1000;
    // permission 관련 끝

    // 회원가입 관련
    int ACTIVITY_DELIVER_REGIST = 10000;
    int REGIST_DLVR_IMG = 10001;
    int REGIST_DLVR_IDCARD = 10002;
    int REGIST_DLVR_BIRTH = 10003;
    int REGIST_DLVR_ADDR = 10004;

    // 회원정보
    int SHOW_DLVR_INFO = 10100;
    int SHOW_MOD_DLVR = 10101;
    int SHOW_DELIVERY = 10102;
    int MOD_DLVR_INFO = 10110;
    int MOD_DLVR_IMG = 10111;
    int MOD_DLVR_ADDR = 10112;

    // 배달 관련
    int PICK_ORDER = 10200;
    int MESSAGE_IMAGE = 10201;

    // 타임라인 관련
    int ALL_TIMELINE = 10300;
    int NOTICE_TIMELINE = 10301;
    int REVIEW_TIMELINE = 10302;
    int FREE_TIMELINE = 10303;
    int WRITE_TIME_IMG = 10310;
    int MOD_TIME_IMG = 10311;

    // s3 관련
    // 이미지 앞주소
    String IMAGE_ADDRESS = "https://delivery-img2.s3.ap-northeast-2.amazonaws.com/";

    // 버킷 내 폴더명
    String DLVR_IMG = "Dlvr_Img/";
    String DLVR_IDCARD = "Dlvr_IDCard/";
    String TIMELINE_IMG = "Timeline_Img/";
    String MESSAGE_IMG = "Message_Img/";
    // s3 관련 끝
}
