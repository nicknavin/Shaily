package com.app.session.interfaces;

import com.app.session.model.Category;
import com.app.session.model.Language;

/**
 * Created by Vikas Sharma on 14/11/16.
 */
public interface DefaultCallback {
    void result(int x);
    void getCategory(Category category);
    void getLanguage(Language language);

}
