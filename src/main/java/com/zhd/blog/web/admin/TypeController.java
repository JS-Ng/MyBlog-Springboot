package com.zhd.blog.web.admin;

import com.zhd.blog.po.Type;
import com.zhd.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
//    @Qualifier("TypeServiceImpl")
    private TypeService typeService;

    @GetMapping("/types")
    public String list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                   Pageable pageable, Model model){
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type", new Type());
        return "admin/type-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type", typeService.getType(id));
        return "admin/type-input";
    }

    @PostMapping("/types")
    //type和BindingResult要挨在一起
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){
        Type type1 = typeService.getTypeByName(type.getName());
        if(type1 != null){
            result.rejectValue("name", "nameError", "The type you add exist!");
        }
        if(result.hasErrors()){
            return "admin/type-input";
        }
        Type t = typeService.saveType(type);
        if(t == null){
            attributes.addFlashAttribute("message", "add failed!");
        }else{
            attributes.addFlashAttribute("message", "add successful!");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    //type和BindingResult要挨在一起
    public String editPost(@Valid Type type, BindingResult result,
                           @PathVariable Long id, RedirectAttributes attributes){
        Type type2 = typeService.getTypeByName(type.getName());
        if(type2 != null){
            result.rejectValue("name", "nameError", "The type you add exist!");
        }
        if(result.hasErrors()){
            return "admin/type-input";
        }
        Type t = typeService.updateType(id, type);
        if(t == null){
            attributes.addFlashAttribute("message", "update failed!");
        }else{
            attributes.addFlashAttribute("message", "update successful!");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        attributes.addFlashAttribute("message", "delete successful!");
        typeService.deleteType(id);
        return "redirect:/admin/types";
    }



}
