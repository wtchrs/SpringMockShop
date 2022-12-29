package wtchrs.SpringMockShop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import wtchrs.SpringMockShop.controller.form.MemberForm;
import wtchrs.SpringMockShop.domain.Address;
import wtchrs.SpringMockShop.domain.Member;
import wtchrs.SpringMockShop.service.MemberService;

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

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member = new Member(memberForm.getUsername(), address);

        memberService.join(member);

        return "redirect:/members";
    }

    @GetMapping("/members")
    public String memberList(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "members/list";
    }
}
