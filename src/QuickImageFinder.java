import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class QuickImageFinder{

    public class Point{
        int x, y;

        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" + "x=" + x + ", y=" + y + '}';
        }
    }

    private String image1s, image2s;
    private File image1File, image2File;
    private BufferedImage image1, image2, smaller, larger;
    private Map<BufferedImage, Point> subImages;
    private Set<Point> outputs;


    public QuickImageFinder(String image1s, String image2s){
        this.image1s = image1s;
        this.image2s = image2s;
        this.image1File = null;
        this.image2File = null;
        this.image1 = null;
        this.image2 = null;
        this.subImages = null;
        this.outputs = null;
    }

    public boolean processImages() {
        image1File = new File(image1s);
        image2File = new File(image2s);

        try{
            image1 = ImageIO.read(image1File);
            image2 = ImageIO.read(image2File);
        }
        catch(IOException ioException){
            System.out.println("Loading Images Failed!");
            return false;
        }

        if (image1.getWidth() * image1.getHeight() < image2.getWidth() * image2.getHeight()) {
            smaller = image1.getSubimage(0, 0, image1.getWidth(), image1.getHeight());
            larger = image2.getSubimage(0, 0, image2.getWidth(), image2.getHeight());

        } else {
            larger = image1.getSubimage(0, 0, image1.getWidth(), image1.getHeight());
            smaller = image2.getSubimage(0, 0, image2.getWidth(), image2.getHeight());
        }

        image1 = null;
        image2 = null;

        subImages = new HashMap<>();
        outputs = new HashSet<>();

        int[] smallerRGB = smaller.getRGB(0, 0, smaller.getWidth(), smaller.getHeight(), null, 0, smaller.getWidth());

        for(int y = 0; y < larger.getHeight() - smaller.getHeight(); y++){
            for(int x = 0; x < larger.getWidth() - smaller.getWidth(); x++){
                Point currPoint = new Point(x, y);
                BufferedImage currSubImage = larger.getSubimage(x, y, smaller.getWidth(), smaller.getHeight());
                subImages.put(currSubImage, currPoint);
            }
        }

        for(BufferedImage compare : subImages.keySet()){
            int[] subImageRGB = compare.getRGB(0, 0, compare.getWidth(), compare.getHeight(), null, 0, compare.getWidth());
            if(new ArrayComparator().compare(smallerRGB, subImageRGB) == 0){
                System.out.println(subImages.get(compare));
                outputs.add(subImages.get(compare));
            }
        }

        return true;
    }

    public void drawOutput(){
        Graphics2D g = (Graphics2D) larger.getGraphics();

        g.setStroke(new BasicStroke(5));
        g.setColor(Color.RED.brighter());
        for(Point found : outputs)
            g.drawRect(found.getX(), found.getY(), smaller.getWidth(), smaller.getHeight());
        g.dispose();

        RenderedImage renderedImage = larger;

        File file = new File("Outcome.png");
        try {
            ImageIO.write(renderedImage, "png", file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        String fullImage = "src/Images/LOZ_image_full_1.png";
        String partImage = "src/Images/LOZ_image_part_1.png";
        QuickImageFinder findImage = new QuickImageFinder(fullImage, partImage);
        findImage.processImages();
        findImage.drawOutput();
    }
}
