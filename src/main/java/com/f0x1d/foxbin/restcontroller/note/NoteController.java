package com.f0x1d.foxbin.restcontroller.note;

import com.f0x1d.foxbin.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NoteController {

    @Autowired
    private NoteService mNoteService;

    @GetMapping("/{slug}")
    public String rawNote(@PathVariable String slug, Model model) {
        String text = mNoteService.getRawNote(slug);

        model.addAttribute("slug", slug);
        model.addAttribute("text", text);
        return "note";
    }
}
