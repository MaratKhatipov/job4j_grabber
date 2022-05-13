package ru.job4j.grabber;

import ru.job4j.grabber.model.Post;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            cnn = DriverManager.getConnection(
                    cfg.getProperty("db.url"),
                    cfg.getProperty("db.username"),
                    cfg.getProperty("db.password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = cnn.prepareStatement(
                "insert into post (name, text, link, created) values (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            int count = statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> items = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement("select * from post")) {
            Post item;
            try (ResultSet resultSet = statement.executeQuery()) {
                while ((item = getPostFromResultSet(resultSet)) != null) {
                    items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    private Post getPostFromResultSet(ResultSet resultSet) throws SQLException {
        Post item = null;
        if (resultSet.next()) {
            item = new Post(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("link"),
                    resultSet.getString("text"),
                    resultSet.getTimestamp("created").toLocalDateTime()
            );
        }
        return item;
    }

    @Override
    public Post findById(int id) {
        Post item = null;
        try (PreparedStatement statement = cnn.prepareStatement("select * from post where id=?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                item = getPostFromResultSet(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        try (InputStream inputStream = PsqlStore.class
                .getClassLoader()
                .getResourceAsStream("grabber.properties")) {
            Properties cfg = new Properties();
            cfg.load(inputStream);
            try (PsqlStore store = new PsqlStore(cfg)) {
                Post post1 = new Post("title1",
                        "link1",
                        "description",
                        LocalDateTime.now()
                );
                store.save(post1);
                Post post2 = new Post("title2",
                        "link2",
                        "description",
                        LocalDateTime.now()
                );
                store.save(post2);
                System.out.println("post2 = " + post2);
                store.getAll().forEach(System.out::println);
                System.out.println(store.findById(12));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

