package ru.egor.tgBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.egor.tgBot.entity.Category;
import ru.egor.tgBot.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getParentCategories(){
        return categoryRepository.findByParentIsNull();
    }

    public List<Category> getChildCategories(Long parentId){
        return categoryRepository.findByParentId(parentId);
    }
    public Category getCategoryByName(String name){
        return categoryRepository.findByName(name);
    }


}
