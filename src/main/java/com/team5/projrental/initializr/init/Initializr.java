package com.team5.projrental.initializr.init;

import com.team5.projrental.entities.ChatBot;
import com.team5.projrental.initializr.repository.ChatBotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Initializr {

    private final ChatBotRepository chatBotRepository;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        chatBotRepository.save(ChatBot.builder()
                .id(1L)
                .grp(1)
                .depth(1)
                .level(1)
                .mention("거래")
                .build());


        chatBotRepository.save(ChatBot.builder()
                .id(2L)
                .grp(1)
                .depth(2)
                .level(1)
                .mention("대여 방법")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(3L)
                .grp(1)
                .depth(3)
                .level(1)
                .mention("희망하는 거래 날짜를 선택하고, 결제한 후, 판매자와 채팅으로 약속 시간을 정한 뒤 거래장소에서 만나서 식별코드를 입력하면 물건을 대여할 수 있습니다.")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(4L)
                .grp(1)
                .depth(2)
                .level(2)
                .mention("반납 방법")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(5L)
                .grp(1)
                .depth(3)
                .level(2)
                .mention("판매자와 채팅으로 시간을 정한 뒤 거래장소에서 만나 물건을 반납한 후 식별코드를 입력하면 거래가 종료됩니다.")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(6L)
                .grp(1)
                .depth(2)
                .level(3)
                .mention("상품 등록")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(7L)
                .grp(1)
                .depth(3)
                .level(3)
                .mention("상품을 등록하려면 로그인 후 \"상품 등록\" 버튼을 클릭하고, 상품 정보를 입력한 후에 등록하시면 됩니다.")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(8L)
                .grp(2)
                .depth(1)
                .level(1)
                .mention("결제")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(9L)
                .grp(2)
                .depth(2)
                .level(1)
                .mention("결제 방법")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(10L)
                .grp(2)
                .depth(3)
                .level(1)
                .mention("원하는 거래 날짜를 선택한 후 \"결제하기\" 버튼을 클릭하고, 결제 수단을 선택하여 결제하면 됩니다.")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(11L)
                .grp(2)
                .depth(2)
                .level(2)
                .mention("결제 취소 및 환불")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(12L)
                .grp(2)
                .depth(3)
                .level(2)
                .mention("마이페이지의 \"예약 내역\" 에서 대여가 시작되지 않은 상품들은 결제 취소가 가능합니다.\n결제 취소를 원하는 경우, 해당 예약 내역을 찾아 취소 옵션을 선택하면 간단한 절차를 " +
                         "통해 예약을 취소하고 환불처리를 진행할 수 있습니다.")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(13L)
                .grp(2)
                .depth(2)
                .level(3)
                .mention("환불 상세")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(14L)
                .grp(2)
                .depth(3)
                .level(3)
                .mention("- 7일 이상 남았을 경우: 별도의 차감 없이 예약을 취소할 수 있습니다.\n" +
                         "- 당일 ~ 3일 남았을 경우: 전체 대여금은 환불되지 않습니다.\n" +
                         "- 4일 ~ 7일 남았을 경우: 대여금의 50%가 환불됩니다.\n" +
                         "- 대여가 이미 시작된 경우에는 정책에 따라 결제 취소가 불가능 합니다.")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(15L)
                .grp(3)
                .depth(1)
                .level(1)
                .mention("예약")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(16L)
                .grp(3)
                .depth(2)
                .level(1)
                .mention("예약 하기")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(17L)
                .grp(3)
                .depth(3)
                .level(1)
                .mention("상품 상세 페이지에서 구매자는 편리하게 희망하는 거래 날짜를 선택하고 원하는 옵션을 결제 페이지에서 확인한 후 결제를 진행합니다.\n" +
                         "예약이 완료되면, 구매자는 마이 페이지의 \"예약 내역\" 에서 손쉽게 예약 정보를 확인할 수 있습니다.\n" +
                         "이를 통해 언제든지 거래 일정을 확인하고 관리할 수 있습니다.")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(18L)
                .grp(3)
                .depth(2)
                .level(2)
                .mention("예약 취소")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(19L)
                .grp(3)
                .depth(3)
                .level(2)
                .mention("마이페이지의 \"예약 내역\" 에서 대여가 시작되지 않은 상품들은 예약 취소가 가능합니다.\n" +
                         "예약 취소를 원하는 경우, 해당 예약 내역을 찾아 취소 옵션을 선택하면 간단한 절차를 통해 예약을 취소할 수 있습니다.\n" +
                         "다만, 대여가 이미 시작된 경우에는 정책에 따라 예약 취소가 불가능 합니다.")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(20L)
                .grp(4)
                .depth(1)
                .level(1)
                .mention("신고")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(21L)
                .grp(4)
                .depth(2)
                .level(1)
                .mention("게시글 신고")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(22L)
                .grp(4)
                .depth(3)
                .level(1)
                .mention("등록된 상품 게시물에서 \"신고하기\" 버튼을 클릭한 후, 신고 창에서 신고 카테고리와 내용을 입력한 후 \"신고하기\" 버튼을 클릭하면 됩니다.")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(23L)
                .grp(4)
                .depth(2)
                .level(2)
                .mention("분쟁 및 사고 신고")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(24L)
                .grp(4)
                .depth(3)
                .level(2)
                .mention("채팅에서 \"신고하기\" 버튼을 클릭한 후, 신고 창에서 카테고리와 내용을 입력하고 \"신고하기\" 버튼을 클릭하면 신고가 가능합니다.")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(25L)
                .grp(5)
                .depth(1)
                .level(1)
                .mention("회원")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(26L)
                .grp(5)
                .depth(2)
                .level(1)
                .mention("ID 찾기")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(27L)
                .grp(5)
                .depth(3)
                .level(1)
                .mention("회원 아이디를 잊어버리셨다면, \n메인 페이지 -> 로그인 -> 아이디 찾기\n 페이지에서 회원 가입 시 등록한 정보를 입력하여 등록정보와 일치하는 회원 아이디를 확인할 수 있습니다.")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(28L)
                .grp(5)
                .depth(2)
                .level(2)
                .mention("회원 가입")
                .build());
        chatBotRepository.save(ChatBot.builder()
                .id(29L)
                .grp(5)
                .depth(3)
                .level(2)
                .mention("회원 가입 시에는 사용자 확인을 위해 모든 필수 항목을 정확하게 기입하셔야 합니다.\n" +
                         "또한, 회원의 안전한 이용을 위해 본인인증을 꼭 완료해주셔야 합니다.\n" +
                         "회원 가입이 성공적으로 완료되면, 등록된 아이디와 비밀번호를 입력하여 로그인이 가능해집니다.\n" +
                         "로그인 후에는 마이페이지의 회원정보 수정을 통해 추가 정보를 입력하거나 프로필을 수정할 수 있습니다.")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(30L)
                .grp(5)
                .depth(2)
                .level(3)
                .mention("회원 탈퇴")
                .build());

        chatBotRepository.save(ChatBot.builder()
                .id(31L)
                .grp(5)
                .depth(3)
                .level(3)
                .mention("탈퇴 시에는 로그인 후 마이 페이지에서 회원 탈퇴를 선택하여 간단하게 탈퇴를 진행할 수 있습니다.\n" +
                         "주의사항: 만약 아직 처리되지 않은 거래가 남아있는 경우에는 탈퇴를 진행할 수 없습니다.\n" +
                         "탈퇴 전에 해당 사항을 확인하시기 바랍니다.")
                .build());





    }

}
