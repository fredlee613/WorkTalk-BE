package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.*;
import com.golfzonTech4.worktalk.dto.customercomment.CustomerCommentInsertDto;
import com.golfzonTech4.worktalk.dto.customercomment.CustomerCommentUpdateDto;
import com.golfzonTech4.worktalk.dto.qnacomment.QnaCommentInsertDto;
import com.golfzonTech4.worktalk.dto.qnacomment.QnaCommentUpdateDto;
import com.golfzonTech4.worktalk.repository.CustomerCenterRepository;
import com.golfzonTech4.worktalk.repository.CustomerCommentRepository;
import com.golfzonTech4.worktalk.repository.QnaCommentRepository;
import com.golfzonTech4.worktalk.repository.QnaRepository;
import com.golfzonTech4.worktalk.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerCommentService {

    private final CustomerCenterRepository customerCenterRepository;
    private final CustomerCommentRepository customerCommentRepository;
    private final MemberService memberService;

    @Transactional
    public CustomerComment createCustomerComment(CustomerCommentInsertDto dto){
        log.info("createCustomerComment()....");
        Optional<String> member = SecurityUtil.getCurrentUsername();

        if(!member.isPresent()) throw new EntityNotFoundException("Member Not Found");

        Member findName = memberService.findByName(member.get());
        CustomerCenter findCcId = customerCenterRepository.findByCcId(dto.getCcId());

        CustomerComment customercommentToCreate = new CustomerComment();
        BeanUtils.copyProperties(dto, customercommentToCreate);//나중에 수정하기
        customercommentToCreate.setCustomerCenter(findCcId);

        return  customerCommentRepository.save(customercommentToCreate);
    }

    @Transactional
    public void updateCustomerComment(Long ccId, CustomerCommentUpdateDto dto) {
        log.info("updateCustomerComment()....");

        Optional<CustomerComment> optionalCustomerComment = Optional.ofNullable(customerCommentRepository.findByCcId(ccId));

            CustomerComment customerComment = optionalCustomerComment.get();
            customerComment.setContent(dto.getContent());//dirty checking

    }

    @Transactional
    public void deleteCustomerComment(Long ccId) {
        log.info("deleteQnaComment()....");
        customerCommentRepository.deleteById(ccId);
    }
}
