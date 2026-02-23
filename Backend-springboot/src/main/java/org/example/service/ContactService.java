package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Contact;
import org.example.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    /* === READ === */
    public List<Contact> getContactList(Integer userId){
        return contactRepository.findContactById(userId);
    }

}
