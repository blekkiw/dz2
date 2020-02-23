package ee.blakcat.Controllers;

import com.google.common.base.Strings;
import ee.blakcat.Models.User;

public interface UserController {


    public String findById (String id) ;

    public String save (User user) ;

    public String findAll ( ) ;

}
