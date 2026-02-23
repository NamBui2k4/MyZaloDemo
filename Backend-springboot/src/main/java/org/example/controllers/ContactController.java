package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.dto.response.ProfileResponse;
import org.example.dto.response.UserResponseDTO;
import org.example.entity.Contact;
import org.example.entity.User;
import org.example.service.ContactService;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/contacts")
@AllArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @GetMapping
    public List<ProfileResponse.ContactDto> getContacts(Principal principal) {
        Integer currentUserId = Integer.parseInt(principal.getName());
        List<Contact> contactList = contactService.getContactList(currentUserId);

        return contactList
                .stream() .map(
                        contact -> new ProfileResponse.ContactDto(
                                contact.getContactName())
                ) .toList();
    }
}
