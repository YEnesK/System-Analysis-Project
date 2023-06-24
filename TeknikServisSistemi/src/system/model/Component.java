package system.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Component {
    private int id;
    private String code;
    private final String name;
    private int stock;
    private final int price;

    public Component(String code, String name, int stock, int price) {
        this.code = code;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public Component(int id, String code, String name, int stock, int price) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public static Component fromResultSet(ResultSet rs) throws SQLException {
        return new Component(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getInt(5),
                rs.getInt(4)
        );
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public int getPrice() {
        return price;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                '}';
    }
}
