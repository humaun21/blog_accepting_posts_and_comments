package course;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;
    int check=0; 
    int checkComment=0;
    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public Document findByPermalink(String permalink) {

        // XXX HW 3.2,  Work Here
        String makelink = permalink.replaceAll("\\_", " "); // _becomes  whitespace
        System.out.println("permalink from find by:"+makelink);
        Document post = null;
       // post=(Document) postsCollection.find(eq("permalink",  makelink)).first();
        //System.out.println("post:"+post.toJson());*/
    	//Document post = null;
    	post = postsCollection.find(new Document("permalink", permalink)).sort(new Document("date", -1)).first();
        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
	public List<Document> findByDateDescending(int limit) {

        // XXX HW 3.2,  Work Here
        // Return a list of DBObjects, each one a post from the posts collection
        //List<Document> posts = null;
    	List<Document> posts =postsCollection.find().limit(limit).sort(new Document("date", -1)).into(new ArrayList<Document>());
    	System.out.println("post:"+posts);
        return posts;
    }
	
    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);
        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        System.out.println("whitespace becomes _:"+permalink);
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        System.out.println("whitespace get rid of non alphanumeric :"+permalink);
        permalink = permalink.toLowerCase();


        // XXX HW 3.2, Work Here
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        // Build the post object and insert it
        // create a calendar instance, and get the date from that
        // instance; it defaults to "today", or more accurately,
        // "now".
        //Date today = Calendar.getInstance().getTime();
         Date now = new Date();
         List<Document> comments = new ArrayList<Document>();
         Document post = new Document();
 	     post.append("author",username).append("title",title).append("body",body).append("permalink",permalink).append("tags",tags).append("date",now).append("comments", comments);
 	     postsCollection.insertOne(post);
 	     return permalink;
    }
    // White space to protect the innocent
    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        // XXX HW 3.3, Work Here
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments.
    	  Document comment = new Document().append("author",name).append("body",body);
    	  if(email != null){
    	  comment.append("email",email);
  }
	postsCollection.updateOne(new Document("permalink",permalink),new Document("$push",new Document("comments",comment)));
    }
}
