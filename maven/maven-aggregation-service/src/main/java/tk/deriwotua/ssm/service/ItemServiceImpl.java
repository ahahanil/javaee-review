package tk.deriwotua.ssm.service;

import tk.deriwotua.ssm.dao.ItemMapper;
import tk.deriwotua.ssm.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

    public Item findById(int id) {
        return itemMapper.findById(id);
    }
}
