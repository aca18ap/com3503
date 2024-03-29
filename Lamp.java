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
  private Light light, bulb;
  private Table table;

  public boolean lymotion, lzmotion, uzmotion;


  private double startTime = getSeconds();

  public Lamp(GL3 gl, Camera camera, Light light, Table table){

    this.camera = camera;
    //light = new Light(gl);
    this.table = table;
    this.light = light;
    bulb = new Light(gl);





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
    TransformNode lampRootTranslate = new TransformNode("lamp transform", Mat4Transform.translate(-0.7f,table.getTableHeight()+baseHeight/2,-1.5f));

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



  public void retractLamp(){
    setLampPosition(20f,65f,-150f);
  }

  public void defaultLamp(){
    setLampPosition(-20f,30f,-80f);
  }


  public void randomLamp(Vec3 v){
    setLampPosition(v.x, v.y, v.z);
  }


  public void setLampPosition(float lowYtarget, float lowZtarget, float upZtarget){
    float currentLowY = lowerYrotation;
    float currentLowZ = lowerZrotation;
    float currentUpZ = upperZrotation;
    if(!((lowYtarget-1f < currentLowY) && (currentLowY < lowYtarget+1.0f))) {
      lymotion = true;
      if(currentLowY > lowYtarget){lowerYrotation -= 1.0f;}
      else{lowerYrotation += 1.0f;}
      rotateLowerY.setTransform(Mat4Transform.rotateAroundY(lowerYrotation));
      rotateLowerY.update();
    }else{
      lymotion=false;
    }

    if(!((lowZtarget-1.0f < currentLowZ) && (currentLowZ < lowZtarget+1.0f))){
      lzmotion=true;
      if(currentLowZ > lowZtarget){lowerZrotation -= 1.0f;}
      else{lowerZrotation += 1.0f;}
      rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(lowerZrotation));
      rotateLowerZ.update();
    }else{
      lzmotion=false;
    }

    if(!((upZtarget-1.0f < currentUpZ) && (currentUpZ < upZtarget+1.0f))){
      uzmotion=true;
      if(currentUpZ > upZtarget){upperZrotation -= 1.0f;}
      else{upperZrotation += 1.0f;}
      rotateUpperZ.setTransform(Mat4Transform.rotateAroundZ(upperZrotation));
      rotateUpperZ.update();
    }else{
      uzmotion=false;
    }

  }

  public Vec3 getLampPosition(){
    Vec3 pos = new Vec3(lowerZrotation, lowerYrotation, upperZrotation);
    return pos;
  }

  public boolean getIfMotion(){
    if(!lymotion && !lzmotion && !uzmotion){
      return false;
    }else{return true;}
  }

  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  public void lightOff(){
    Material material = bulb.getMaterial();
    material.setAmbient(0.1f,0.1f,0.1f);
    material.setDiffuse(0.2f,0.2f,0.2f);
    material.setEmission(0.1f,0.1f,0.1f);
    material.setSpecular(0.1f,0.1f,0.1f);
    material.setShininess(1f);
    this.bulb.setMaterial(material);
  }

  public void lightOn(){
    Material material = bulb.getMaterial();
    material.setAmbient(0.5f,0.5f,0.5f);
    material.setDiffuse(0.8f,0.8f,0.8f);
    material.setEmission(1.0f,1.0f,1.0f);
    material.setShininess(32f);
    this.bulb.setMaterial(material);
  }


  public void setInMotion(){
    lymotion = true;
  }
  public void dispose(GL3 gl){
    base.dispose(gl);
    lowerArm.dispose(gl);
    upperArm.dispose(gl);
    head.dispose(gl);
  }
}
