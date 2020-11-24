import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Lamp {

  private float baseWidth = 0.1f;
  private float baseDepth = 0.1f;
  private float baseHeight = 0.03f;

  private float lowerArmRadius = 0.05f;
  private float lowerArmHeight = 0.3f;

  private float upperArmRadius = 0.05f;
  private float upperArmHeight = 0.3f;

  private float headWidth = 0.05f;
  private float headDepth = 0.05f;
  private float headHeight = 0.05f;

  private float lowerXrotation = 10, lowerYrotation = 10, upperXrotation = 45, headXrotation = 10;

  private TransformNode rotateLowerY, rotateLowerX, rotateUpperX;

  private Model base, lowerArm, upperArm, head;
  private SGNode lampRoot;

  private Camera camera;
  private Light light;

  private double startTime = getSeconds();

  public Lamp(GL3 gl, Camera camera, Light light){

    this.camera = camera;
    this.light = light;


    int[] textureId0 = TextureLibrary.loadTexture(gl, "./textures/jup0vss1.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "./textures/mar0kuu2.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "./textures/container2.jpg");

    base = initBase(gl, textureId2);
    lowerArm = initArm(gl, textureId0);
    upperArm = initArm(gl, textureId1);
    head = initBase(gl, textureId0);



    lampRoot = new NameNode("lampRoot");
    TransformNode lampRootTranslate = new TransformNode("lamp transform", Mat4Transform.translate(0,1,0));

    NameNode baseName = new NameNode("base");
      Mat4 m = Mat4Transform.scale(baseWidth, baseHeight, baseDepth);
      TransformNode baseScale = new TransformNode("base scale", m);
      TransformNode baseTransform = new TransformNode("base transform", m);
        ModelNode baseShape = new ModelNode("Cube(0)", base);

    NameNode lowerArmName = new NameNode("lowerArm");
      TransformNode lowerArmTranslate = new TransformNode("lower arm translate",Mat4Transform.translate(0,lowerArmHeight/2,0));
      rotateLowerX = new TransformNode("lower arm rotate X", Mat4Transform.rotateAroundX(lowerXrotation));
      rotateLowerY = new TransformNode("lower arm rotate Y", Mat4Transform.rotateAroundY(lowerYrotation));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(lowerArmRadius, lowerArmHeight, lowerArmRadius));
      TransformNode lowerArmScale = new TransformNode("lower arm scale", m);
        ModelNode lowerArmShape = new ModelNode("Sphere(0)", lowerArm);


    NameNode upperArmName = new NameNode("upperArm");
      TransformNode upperArmTranslate = new TransformNode("upper arm translate", Mat4Transform.translate(0,upperArmHeight+lowerArmHeight+baseHeight,0));
      rotateUpperX = new TransformNode("upper arm rotate X", Mat4Transform.rotateAroundX(upperXrotation));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(upperArmRadius, upperArmHeight, upperArmHeight));
      TransformNode upperArmScale = new TransformNode("upper arm scale", m);
        ModelNode upperArmShape = new ModelNode("Sphere(1)", upperArm);


    lampRoot.addChild(lampRootTranslate);
      lampRootTranslate.addChild(baseName);
        baseName.addChild(baseScale);
        baseScale.addChild(baseShape);
      lampRootTranslate.addChild(lowerArmName);
      lowerArmName.addChild(rotateLowerX);
        rotateLowerX.addChild(rotateLowerY);
        rotateLowerY.addChild(lowerArmTranslate);
        lowerArmTranslate.addChild(lowerArmScale);
        lowerArmScale.addChild(lowerArmShape);


    lampRoot.update();
    lampRoot.print(0, false);




  }

  private Model initBase(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_cube.txt", "./shaders/fs_cube.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model base = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return base;
  }


  private Model initArm(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_cube.txt", "./shaders/fs_cube.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model arm = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return arm;
  }

  public void draw(GL3 gl){
    lampRoot.draw(gl);
  }

  public void updateLowerArmX(){
    double elapsedTime = getSeconds()-startTime;
    float rotateAngle = 30f*(float)Math.sin(elapsedTime);
    rotateLowerX.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    rotateLowerX.update();
  }

  public void updateLowerArmY(){
    double elapsedTime = getSeconds()-startTime;
    float rotateAngle = 30f*(float)Math.sin(elapsedTime);
    rotateLowerY.setTransform(Mat4Transform.rotateAroundY(rotateAngle));
    rotateLowerY.update();
  }

  public void updateUpperArmX(){
    double elapsedTime = getSeconds()-startTime;
    float rotateAngle = 60f*(float)Math.sin(elapsedTime);
    rotateUpperX.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    rotateUpperX.update();
  }


  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
}
