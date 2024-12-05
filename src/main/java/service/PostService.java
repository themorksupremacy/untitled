package service;

import org.example.model.Post;
import org.example.repository.PostRepository;

import java.util.List;

public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Post getPostById(long id){
        return postRepository.getById(id);
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Post post) {
        postRepository.deleteById(post.getId());
    }
}
