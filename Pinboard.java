import gmaths.*;
import com.jogamp.opengl.*;

public final class Pinboard {

  private float height = 1.0f;
  private float width = 2.0f;
  private float depth = 0.02f;

  private Camera camera;
  private Light light;
  private Model pinboard, pic1, pic2, pic3;

  private SGNode pinRoot;

  public Pinboard(GL3 gl, Camera camera, Light light){
    System.out.println("Pinboard init");

    this.camera = camera;
    this.light = light;

    int[] textureId0 = TextureLibrary.loadTexture(gl, "./textures/pinboard.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "./textures/sunset.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "./textures/flower.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "./textures/birthday.jpg");


    pinboard = initBoard(gl, textureId0);
    pic1 = initPictures(gl, textureId1);
    pic2 = initPictures(gl, textureId2);
    pic3 = initPictures(gl, textureId3);

    pinRoot = new NameNode("pinboard root");
    TransformNode  pinRootTranslate = new TransformNode("pinboard translate", Mat4Transform.translate(0, 1.8f, -2.0f));

    NameNode pinboardName = new NameNode("pinboard");
      Mat4 m = Mat4Transform.scale(width, height, depth);
      TransformNode pinboardScale = new TransformNode("pinboard transform", m);
        ModelNode pinboardShape = new ModelNode("Cube(0)", pinboard);

    NameNode pic1name = new NameNode("pic1");
      TransformNode pic1translate = new TransformNode("pic1translate", Mat4Transform.translate(-0.6f,0.02f, -0.2f));
      TransformNode pic1rotateX = new TransformNode("pic1rotateX", Mat4Transform.rotateAroundX(90));
      TransformNode pic1rotateY = new TransformNode("pic1rotateY", Mat4Transform.rotateAroundY(4));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.2f,0.01f,0.33f));
      TransformNode pic1scale = new TransformNode("pic1 transform", m);
        ModelNode pic1shape = new ModelNode("Plane(0)",pic1);

    NameNode pic2name = new NameNode("pic2");
      TransformNode pic2translate = new TransformNode("pic1translate", Mat4Transform.translate(0.4f,0.02f, 0.1f));
      TransformNode pic2rotateX = new TransformNode("pic1rotateX", Mat4Transform.rotateAroundX(90));
      TransformNode pic2rotateY = new TransformNode("pic1rotateY", Mat4Transform.rotateAroundY(-6));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.2f,0.01f,0.33f));
      TransformNode pic2scale = new TransformNode("pic2 transform", m);
        ModelNode pic2shape = new ModelNode("Plane(0)",pic2);

    NameNode pic3name = new NameNode("pic3");
      TransformNode pic3translate = new TransformNode("pic3translate", Mat4Transform.translate(-0.1f,0.02f, 0.0f));
      TransformNode pic3rotateX = new TransformNode("pic3rotateX", Mat4Transform.rotateAroundX(90));
      TransformNode pic3rotateY = new TransformNode("pic3rotateY", Mat4Transform.rotateAroundY(1));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.2f,0.01f,0.33f));
      TransformNode pic3scale = new TransformNode("pic3 transform", m);
        ModelNode pic3shape = new ModelNode("Plane(0)",pic3);

    pinRoot.addChild(pinRootTranslate);
      pinRootTranslate.addChild(pinboardName);
        pinboardName.addChild(pinboardScale);
          pinboardScale.addChild(pinboardShape);
        pinRootTranslate.addChild(pic1name);
          pic1name.addChild(pic1rotateX);
          pic1rotateX.addChild(pic1rotateY);
          pic1rotateY.addChild(pic1translate);
          pic1translate.addChild(pic1scale);
          pic1scale.addChild(pic1shape);
        pinRootTranslate.addChild(pic2name);
          pic2name.addChild(pic2rotateX);
          pic2rotateX.addChild(pic2rotateY);
          pic2rotateY.addChild(pic2translate);
          pic2translate.addChild(pic2scale);
          pic2scale.addChild(pic2shape);
        pinRootTranslate.addChild(pic3name);
          pic3name.addChild(pic3rotateX);
          pic3rotateX.addChild(pic3rotateY);
          pic3rotateY.addChild(pic3translate);
          pic3translate.addChild(pic3scale);
          pic3scale.addChild(pic3shape);

    pinRoot.update();


  }

  private Model initBoard(GL3 gl, int[] t){
      Mesh m = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
      Shader shader = new Shader(gl, "./shaders/vs_cube.txt", "./shaders/fs_cube.txt");
      Material material = new Material(new Vec3(0.2f, 0.2f, 0.2f), new Vec3(0.2f, 0.2f, 0.2f), new Vec3(0.2f, 0.2f, 0.2f), 0.5f);
      Model board = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
      return board;
  }

  private Model initPictures(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Plane.vertices.clone(), Plane.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_plane.txt", "./shaders/fs_plane.txt");
    Material material = new Material(new Vec3(0.6f, 0.6f, 0.6f), new Vec3(0.6f, 0.6f, 0.6f), new Vec3(0.6f, 0.6f, 0.6f), 1.0f);
    Model picture = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return picture;
  }

  public void draw(GL3 gl){
    pinRoot.draw(gl);
  }

}
