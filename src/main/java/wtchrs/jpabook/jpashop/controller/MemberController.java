package wtchrs.jpabook.jpashop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import wtchrs.jpabook.jpashop.controller.form.MemberForm;
import wtchrs.jpabook.jpashop.domain.Address;
import wtchrs.jpabook.jpashop.domain.Member;
import wtchrs.jpabook.jpashop.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String joinForm(@ModelAttribute("memberForm") MemberForm memberForm) {
        return "members/join";
    }

    @PostMapping("/members/new")
    public String joinProcess(@ModelAttribute("memberForm") @Validated MemberForm memberForm, Errors errors) {
        if (errors.hasErrors()) return "members/join";

        Member member = new Member();
        member.setUsername(memberForm.getUsername());
        member.setAddress(new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode()));

        memberService.join(member);

        return "redirect:/members";
    }

    @GetMapping("/members")
    public String memberList(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "members/list";
    }
}
