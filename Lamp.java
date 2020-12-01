import gmaths.*;
import java.util.Random;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Lamp {

  private float baseWidth = 0.15f;
  private float baseDepth = 0.15f;
  private float baseHeight = 0.03f;

  private float lowerArmRadius = 0.05f;
  private float lowerArmHeight = 0.3f;

  private float upperArmRadius = 0.05f;
  private float upperArmHeight = 0.3f;

  private float headWidth = 0.1f;
  private float headDepth = 0.1f;
  private float headHeight = 0.1f;

  private float lowerZrotation = 0, lowerYrotation = 0, upperZrotation = 0, headZrotation = 0;

  private TransformNode rotateLowerY, rotateLowerZ, rotateUpperZ, rotateHeadZ;

  private Model base, lowerArm, upperArm, head, lightBulb;
  private SGNode lampRoot;

  private Camera camera;
  private Light light;
  private Table table;



  private double startTime = getSeconds();

  public Lamp(GL3 gl, Camera camera, Table table){

    this.camera = camera;
    light = new Light(gl);
    this.table = table;



    int[] textureId0 = TextureLibrary.loadTexture(gl, "./textures/jup0vss1.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "./textures/mar0kuu2.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "./textures/container2.jpg");

    base = initBase(gl, textureId2);
    lowerArm = initArm(gl, textureId0);
    upperArm = initArm(gl, textureId1);
    head = initHead(gl, textureId0);
    //lightBulb = initLightBulb(gl);
    // /head.setMaterial.setEmission(1.0f,1.0f,1.0f);



    lampRoot = new NameNode("lampRoot");
    TransformNode lampRootTranslate = new TransformNode("lamp transform", Mat4Transform.translate(-0.7f,table.getTableHeight()+baseHeight/2,-1.8f));

    NameNode baseName = new NameNode("base");
      Mat4 m = Mat4Transform.scale(baseWidth, baseHeight, baseDepth);
      TransformNode baseScale = new TransformNode("base scale", m);
      TransformNode baseTransform = new TransformNode("base transform", m);
        ModelNode baseShape = new ModelNode("Cube(0)", base);

    NameNode lowerArmName = new NameNode("lowerArm");
      TransformNode lowerArmTranslate = new TransformNode("lower arm translate",Mat4Transform.translate(0,lowerArmHeight/2,0));
      rotateLowerZ = new TransformNode("lower arm rotate Z", Mat4Transform.rotateAroundZ(-lowerZrotation));
      rotateLowerY = new TransformNode("lower arm rotate Y", Mat4Transform.rotateAroundY(lowerYrotation));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(lowerArmRadius, lowerArmHeight, lowerArmRadius));
      TransformNode lowerArmScale = new TransformNode("lower arm scale", m);
        ModelNode lowerArmShape = new ModelNode("Sphere(0)", lowerArm);


    NameNode upperArmName = new NameNode("upperArm");
      TransformNode upperArmTranslate = new TransformNode("upper arm translate", Mat4Transform.translate(0,upperArmHeight/2,0));
      rotateUpperZ = new TransformNode("upper arm rotate Z", Mat4Transform.rotateAroundZ(-upperZrotation));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(upperArmRadius, upperArmHeight, upperArmRadius));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode upperArmScale = new TransformNode("upper arm scale", m);
        ModelNode upperArmShape = new ModelNode("Sphere(1)", upperArm);


    NameNode headName = new NameNode("head");
      TransformNode headTranslate = new TransformNode("head translate", Mat4Transform.translate(0,upperArmHeight,0));
      rotateHeadZ = new TransformNode("head rotate Z", Mat4Transform.rotateAroundZ(headZrotation));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(headWidth,headHeight, headDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode headScale = new TransformNode("head scale", m);
        ModelNode headShape = new ModelNode("Cube(0)", head);

    NameNode lightName = new NameNode("light");
      TransformNode lightTranslate = new TransformNode("light translate", Mat4Transform.translate(0.2f,0,0));
      m = new Mat4(1);
      ModelNode lightShape = new ModelNode("Light(0)", head);

    lampRoot.addChild(lampRootTranslate);
      lampRootTranslate.addChild(baseName);
        baseName.addChild(baseScale);
        baseScale.addChild(baseShape);
      lampRootTranslate.addChild(lowerArmName);
      lowerArmName.addChild(rotateLowerY);
        rotateLowerY.addChild(rotateLowerZ);
        rotateLowerZ.addChild(lowerArmTranslate);
        lowerArmTranslate.addChild(lowerArmScale);
        lowerArmScale.addChild(lowerArmShape);

        lowerArmTranslate.addChild(upperArmName);
          upperArmName.addChild(upperArmTranslate);
          upperArmTranslate.addChild(rotateUpperZ);
          rotateUpperZ.addChild(upperArmScale);
          upperArmScale.addChild(upperArmShape);

          rotateUpperZ.addChild(headName);
            headName.addChild(headTranslate);
            headTranslate.addChild(rotateHeadZ);
            rotateHeadZ.addChild(headScale);
            headScale.addChild(headShape);
            //headShape.addChild(lightName);
              //lightName.addChild(lightShape);




    lampRoot.update();
    //lampRoot.print(0, false);




  }

  private Model initBase(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_cube.txt", "./shaders/fs_cube.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model base = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return base;
  }

  private Model initHead(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_cube.txt", "./shaders/fs_cube.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    material.setEmission(1.0f,0.0f,0.0f);
    Model head = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return head;
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

  public void updateLowerArmZ(float angle){
    if(!((angle-1)<lowerZrotation && (angle+1)> lowerZrotation)){
      if (lowerZrotation >= (angle-1f)){
        double elapsedTime = getSeconds()-startTime;
        float rotateAngle = angle*(float)Math.sin(elapsedTime);
        lowerZrotation = rotateAngle;
        rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(-lowerZrotation));
        rotateLowerZ.update();
      }else{
        double elapsedTime = getSeconds()-startTime;
        float rotateAngle = angle*(float)Math.sin(elapsedTime);
        lowerZrotation = rotateAngle;
        rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(-lowerZrotation));
        rotateLowerZ.update();
      }
    }
  }

  public void updateLowerArmY(float angle){
    if(!((angle-1)<lowerYrotation && (angle+1)> lowerYrotation)){
      if (lowerYrotation >= (angle-1f)){
        double elapsedTime = getSeconds()-startTime;
        float rotateAngle = angle*(float)Math.sin(elapsedTime);
        lowerYrotation = rotateAngle;
        rotateLowerY.setTransform(Mat4Transform.rotateAroundY(-lowerYrotation));
        rotateLowerY.update();
      }else{
        double elapsedTime = getSeconds()-startTime;
        float rotateAngle = angle*(float)Math.sin(elapsedTime);
        lowerYrotation = rotateAngle;
        rotateLowerY.setTransform(Mat4Transform.rotateAroundY(-lowerYrotation));
        rotateLowerY.update();
      }
    }
  }

  public void updateUpperArmZ(float angle){
    if(!((angle-1)<upperZrotation && (angle+1)> upperZrotation)){
      if (upperZrotation >= (angle-1f)){
        double elapsedTime = getSeconds()-startTime;
        float rotateAngle = angle*(float)Math.sin(elapsedTime);
        upperZrotation = rotateAngle;
        rotateUpperZ.setTransform(Mat4Transform.rotateAroundZ(-upperZrotation));
        rotateUpperZ.update();
      }else{
        double elapsedTime = getSeconds()-startTime;
        float rotateAngle = angle*(float)Math.sin(elapsedTime);
        upperZrotation = rotateAngle;
        rotateUpperZ.setTransform(Mat4Transform.rotateAroundZ(-upperZrotation));
        rotateUpperZ.update();
      }
    }
  }


  public void setLampPosition(float lowY, float lowZ, float upZ){
    updateLowerArmY(lowY);
    updateLowerArmZ(lowZ);
    updateUpperArmZ(upZ);
  }

  public void retractLamp(){
    updateLowerArmZ(-65f);
    updateUpperArmZ(150f);
    updateLowerArmY(0f);
  }


  public void randomLampPosition(){
    Random random = new Random();
    updateLowerArmZ((-65f + random.nextFloat() * (15f - (-65f))));
    updateUpperArmZ((-65f + random.nextFloat() * (15f - (-65f))));
    updateLowerArmY((-180f + random.nextFloat() * (180f - (-180f))));
  }




  public float getLowerArmZ(){
    return this.lowerZrotation;
  }




  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
}
