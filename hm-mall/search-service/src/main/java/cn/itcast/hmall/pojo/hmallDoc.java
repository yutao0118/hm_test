package cn.itcast.hmall.pojo;

import com.hmall.common.dto.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class hmallDoc {
    private Long id;
    private String name;
    private String category;
    private Long price;
    private Integer sold;
    private String brand;
    private Integer commentCount;
    private String image;
    private Object distance;
    private Boolean isAD;
    private List<String> suggestion;

    public hmallDoc(Item hmall) {
        this.id = hmall.getId();
        this.name = hmall.getName();
        this.category = hmall.getCategory();
        this.price = hmall.getPrice();
        this.sold = hmall.getSold();
        this.brand = hmall.getBrand();
        this.commentCount = hmall.getCommentCount();
        this.image = hmall.getImage();
        this.isAD = hmall.getIsAD();
        // 组装suggestion
        this.suggestion = Arrays.asList(this.brand, this.category);
    }
}