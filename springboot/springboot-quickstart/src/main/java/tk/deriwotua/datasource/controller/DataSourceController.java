package tk.deriwotua.datasource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    DataSource dataSource;

    @GetMapping()
    public String getDataSource(){
        return dataSource.toString();
    }
}
