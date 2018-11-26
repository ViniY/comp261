package code.renderer;

import code.renderer.Scene.Polygon;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Pipeline class has method stubs for all the major components of the
 * rendering pipeline, for you to fill in.
 *
 * Some of these methods can get quite long, in which case you should strongly
 * consider moving them out into their own file. You'll need to update the
 * imports in the test suite if you do.
 */
public class Pipeline {

	/**
	 * Returns true if the given polygon is facing away from the camera (and so
	 * should be hidden), and false otherwise.
	 */
	public static boolean isHidden(Polygon poly) {
		// TODO fill this in.
//		float z1=poly.getVertices()[0].z;
//		float z2=poly.getVertices()[1].z;
//		float z3=poly.getVertices()[2].z;
//		if(z1>0||z2>0||z3>0){
//			return true;
//		}
//		else{return false;}
		Vector3D normal = normal(poly);
		return normal.z>=0;
	}
	public static Vector3D normal(Polygon poly){
		Vector3D v1 = poly.vertices[0];
		Vector3D v2 = poly.vertices[1];
		Vector3D v3 = poly.vertices[2];
		return ((v2.minus(v1)).crossProduct(v3.minus(v2))).unitVector();
	}

	/**
	 * Computes the colour of a polygon on the screen, once the lights, their
	 * angles relative to the polygon's face, and the reflectance of the polygon
	 * have been accounted for.
	 *
	 *
	 *            The Vector3D pointing to the directional light read in from
	 *            the file.
	 * @param lightColor
	 *            The color of that directional light.
	 * @param ambientLight
	 *            The ambient light in the scene, i.e. light that doesn't depend
	 *            on the direction.
	 */
	//public static Color getShading(Polygon poly, Vector3D lightDirection, Color lightColor, Color ambientLight) {
	  public static  Color getShading(Polygon poly,ArrayList<Vector3D>lightDirections,Color lightColor,Color ambientLight)	{
		// TODO fill this in.
		Color reflectance=poly.reflectance;//reflectance of the polygon
        Vector3D normal = normal(poly);
        //float costheta = normal.cosTheta(lightDirection);
		//int red =(int)( (ambientLight.getRed()/255.f)*(reflectance.getRed()/1.f)+ (lightColor.getRed()/255.f)*(reflectance.getRed()/1.f)*costheta);
		//int green =(int) ((ambientLight.getGreen()/255.f)*(reflectance.getGreen()/1.f)+ (lightColor.getGreen()/255.f)*(reflectance.getGreen()/1.f)*costheta);
		//int blue =(int)( (ambientLight.getBlue()/255.f)*(reflectance.getBlue()/1.f)+ (lightColor.getBlue()/255.f)*(reflectance.getBlue()/1.f)*costheta);
        //if( isHidden(poly)) return ambientLight;
		  int finalRed=0;
		  int finalGreen=0;
		  int finalBlue=0;
		  if( isHidden(poly)) return ambientLight;
		  for(Vector3D lightDir : lightDirections) {
			  float costheta = normal.cosTheta(lightDir);
			  //stage 1-3
//			  int red =(int)( (ambientLight.getRed()/255.f)*(reflectance.getRed()/1.f)+ (lightColor.getRed()/255.f)*(reflectance.getRed()/1.f)*costheta);
//			  int green =(int) ((ambientLight.getGreen()/255.f)*(reflectance.getGreen()/1.f)+ (lightColor.getGreen()/255.f)*(reflectance.getGreen()/1.f)*costheta);
//			  int blue =(int)( (ambientLight.getBlue()/255.f)*(reflectance.getBlue()/1.f)+ (lightColor.getBlue()/255.f)*(reflectance.getBlue()/1.f)*costheta);
			//stage 4
			  float red =  (costheta * reflectance.getRed()/1f * (lightColor.getRed() / 255.f));
			  float blue =  (costheta * reflectance.getBlue()/1f * (lightColor.getBlue() / 255.f));
			  float green = (costheta * reflectance.getGreen()/1f * (lightColor.getGreen() / 255.f));
			  red=Math.max(red,0);
			  blue=Math.max(blue,0);
			  green=Math.max(green,0);
			  finalRed += red;
			  finalGreen += green;
			  finalBlue += blue;
			  System.out.println(red + "red");
		  }
		  int Red = (int)(finalRed + (ambientLight.getRed()/255.f)*(reflectance.getRed()/1f));
		  int Green = (int) (finalGreen + (ambientLight.getGreen()/255.f)*(reflectance.getGreen()/1f));
		  int Blue = (int) (finalBlue + (ambientLight.getBlue()/255.f)*(reflectance.getBlue()/1f));
        if(Red>255)Red=255;
        else if(Red<0)Red=0;
		if(Green>255)Green=255;
		else if(Green<0)Green=0;
		if(Blue>255)Blue=255;
		else if(Blue<0)Blue=0;
		return new Color(Red,Green,Blue);
	}

	/**
	 * This method should rotate the polygons and light such that the viewer is
	 * looking down the Z-axis. The idea is that it returns an entirely new
	 * Scene object, filled with new Polygons, that have been rotated.
	 *
	 * @param scene
	 *            The original Scene.
	 * @param xRot
	 *            An angle describing the viewer's rotation in the YZ-plane (i.e
	 *            around the X-axis).
	 * @param yRot
	 *            An angle describing the viewer's rotation in the XZ-plane (i.e
	 *            around the Y-axis).
	 * @return A new Scene where all the polygons and the light source have been
	 *         rotated accordingly.
	 */
	public static Scene rotateScene(Scene scene, float xRot, float yRot) {
		// TODO fill this in.
		//System.out.println(scene.lightSources.get(0));
		if(scene==null) return null;
		ArrayList<Polygon> polygons= new ArrayList<>();
			polygons= (ArrayList<Polygon>)scene.getPolygons();
			Transform xRotate= Transform.newXRotation(xRot);
			Transform yRotate= Transform.newYRotation(yRot);
			ArrayList<Polygon> polys = new ArrayList<>();

			Scene afterRotationScene = new Scene(scene.polygons,scene.getLight());
			afterRotationScene.lightSources=scene.lightSources;
			for(Polygon p : polygons){

				Color col = p.reflectance;
				Vector3D[] afterRotation = p.vertices;
				for(int i=0;i<3;i++){
					if(xRot!=0.0f){//xrot=0 means no rotation
						afterRotation[i]=xRotate.multiply(afterRotation[i]);
				}
					if(yRot!=0.0f){
						afterRotation[i]=yRotate.multiply(afterRotation[i]);
					}
				}
				polys.add(new Polygon(afterRotation[0],afterRotation[1],afterRotation[2],col));
			}
			Vector3D lightDir = scene.getLight();
			//if(xRot!=0.0f){
			//	lightDir= xRotate.multiply(lightDir);
			//}
			//if(yRot!=0.0f) lightDir=yRotate.multiply(lightDir);
		Scene afterRotation = new Scene(polys,lightDir);
		afterRotation.lightSources=scene.lightSources;
			return afterRotation;
	}

	/**
	 * This should translate the scene by the appropriate amount.
	 *
	 * @param scene
	 * @return
	 */
	public static Scene translateScene(Scene scene) {
		// TODO fill this in.
		if(scene==null) return null;
		float xdis=0-BoundingBox(scene)[0];
		float ydis=0-BoundingBox(scene)[2];
		Vector3D translater = new Vector3D(xdis,ydis,0);
		Transform transform= Transform.newTranslation(translater);
		List<Scene.Polygon> polys  = new ArrayList<>();
		for(Polygon p: scene.getPolygons()){
			Vector3D[] polyVector = p.getVertices();
			for(int i=0;i<3;i++){
				polyVector[i]=transform.multiply(polyVector[i]);
			}
			polys.add(new Polygon(polyVector[0],polyVector[1],polyVector[2],p.reflectance));
		}
		Scene afterTranslateScene=new Scene(polys,scene.getLight());
		afterTranslateScene.lightSources=scene.lightSources;
		return afterTranslateScene;
	}

	/**
	 * This should scale the scene.
	 *
	 * @param scene
	 * @return
	 */
	public static Scene scaleScene(Scene scene) {
		// TODO fill this in.

		float width = BoundingBox(scene)[1]-BoundingBox(scene)[0];
		float height = BoundingBox(scene)[3]-BoundingBox(scene)[2];
		float scaleFactor = 1.0f;
		Scene newScene =new Scene(new ArrayList<Polygon>(),scene.getLight());
	    scaleFactor=Math.min(GUI.CANVAS_WIDTH/width,GUI.CANVAS_HEIGHT/height);
		if(scaleFactor==1.0f) return scene;//the graph don;t need to scale
		Transform scaler = Transform.newScale(scaleFactor,scaleFactor,scaleFactor);
		List<Polygon> polygons = scene.getPolygons();
		for(int i=0;i<scene.getPolygons().size();i++){
			Vector3D v1= scaler.multiply(scene.getPolygons().get(i).vertices[0]);
			Vector3D v2=scaler.multiply(scene.getPolygons().get(i).vertices[1]);
			Vector3D v3=scaler.multiply(scene.getPolygons().get(i).vertices[2]);
			newScene.getPolygons().add(new Polygon(v1,v2,v3,scene.polygons.get(i).reflectance));

			}

//		Vector3D lightsource = scene.getLight();
//		lightsource=scaler.multiply(lightsource);
		newScene.lightSources=scene.lightSources;
		return newScene;
	}

	/**
	 * Computes the edgelist of a single provided polygon, as per the lecture
	 * slides.
	 */
	public static EdgeList computeEdgeList(Polygon poly) {
     Vector3D[] edges = poly.getVertices();
     int minY= Integer.MAX_VALUE;
     int maxY= Integer.MIN_VALUE;
     for(Vector3D v: edges) {
		 if (v.y < minY) minY = Math.round(v.y);
		 if (v.y > maxY) maxY = Math.round(v.y);
	 }
	 //System.out.println("minY:"+minY+"maxY"+maxY);
	 EdgeList edgeList = new EdgeList(minY,maxY);
     Vector3D v1;
     Vector3D v2;
     for(int index = 0 ; index<3; index++){
     	v1=edges[index];
     	v2=edges[(index+1)%3];
     	float slopeX= (v2.x-v1.x)/(Math.round(v2.y)-Math.round(v1.y));//slope x
		float slopeZ= (v2.z-v1.z)/(Math.round(v2.y)-Math.round(v1.y));//z slope
		 float positionX = v1.x;
		 float positionZ = v1.z;
     	if(v1.y<v2.y) {//update left
//			int startY = Math.round(v1.y);//the start Y
//			int cursor = Math.round(v1.y);
//			float positionX = v1.x;
//			float positionZ = v1.z;
			int y= Math.round(v1.y);
			while (y<= Math.round(v2.y)) {
				edgeList.edgeData[y][0]=positionX;
				edgeList.edgeData[y][2]=positionZ;
				positionX = positionX + slopeX;
				positionZ = positionZ + slopeZ;
//				cursor++;
				y++;
			}
		}
		else{
			int y=Math.round(v1.y);
			while(y>=Math.round(v2.y)){
				edgeList.edgeData[y][1]=positionX;
				edgeList.edgeData[y][3]=positionZ;
				positionX-=slopeX;
				positionZ-=slopeZ;
				y--;
			}
		}
	 }
		 return edgeList;

	}
	public static float[] BoundingBox(Scene s){
		float miny=Float.MAX_VALUE;
		float maxy=Float.MIN_VALUE;
		float minx=Float.MAX_VALUE;
		float maxx=Float.MIN_VALUE;
		for(Polygon p : s.getPolygons()){
			Vector3D[] vectors=p.getVertices();
			for(Vector3D v : vectors){
				minx= Math.min(minx,v.x);
				miny=Math.min(miny,v.y);
				maxx=Math.max(maxx,v.x);
				maxy=Math.max(maxy,v.y);
			}
		}
		return new float[]{minx,maxx,miny,maxy};
	}

	/**
	 * Fills a zbuffer with the contents of a single edge list according to the
	 * lecture slides.
	 *
	 * The idea here is to make zbuffer and zdepth arrays in your main loop, and
	 * pass them into the method to be modified.
	 *
	 * @param zbuffer
	 *            A double array of colours representing the Color at each pixel
	 *            so far.
	 * @param zdepth
	 *            A double array of floats storing the z-value of each pixel
	 *            that has been coloured in so far.
	 * @param polyEdgeList
	 *            The edgelist of the polygon to add into the zbuffer.
	 * @param polyColor
	 *            The colour of the polygon to add into the zbuffer.
	 */
	public static void computeZBuffer(Color[][] zbuffer, float[][] zdepth, EdgeList polyEdgeList, Color polyColor) {
		// TODO fill this in.
		int EL_ymin = polyEdgeList.getStartY();
		int EL_ymax =polyEdgeList.getEndY();
		for(int y=EL_ymin; y<EL_ymax;y++){
			float slopeZ = (polyEdgeList.getRightZ(y)-polyEdgeList.getLeftZ(y))/(polyEdgeList.getRightX(y)-polyEdgeList.getLeftX(y));
			int x=Math.round(polyEdgeList.getLeftX(y));
			float z=polyEdgeList.getLeftZ(y)+slopeZ*(x-polyEdgeList.getLeftX(y));
            //float z=polyEdgeList.getLeftZ(y);
		while(x<=(Math.round(polyEdgeList.getRightX(y))-1)){
			if(withinArea(x,y)&& z<zdepth[x][y] ){
				zdepth[x][y]=z;
				zbuffer[x][y]=polyColor;
			}
			z+=slopeZ;
			x++;
		}
		}
}
public static boolean withinArea(int x, int y ){
		return (x< GUI.CANVAS_WIDTH && x>=0 && y>=0 && y<GUI.CANVAS_HEIGHT);
}
}

// code for comp261 assignments
