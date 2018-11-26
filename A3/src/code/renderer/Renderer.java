package code.renderer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Renderer extends GUI {
	//field:

	Scene.Polygon polygon=null;
	Scene scene=null;
	private Vector3D lightPos;

	@Override
	protected void onLoad(File file) {
		// TODO fill this in.

		String[] lightSource_unfixed= new String[3];
		ArrayList<Scene.Polygon> polygons= new ArrayList<>();
		float[] lightSource=new float[3];
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			String line = input.readLine();
			lightSource_unfixed = line.split("\\s+");//the coordinate of the light source
					lightSource[0]=Float.parseFloat(lightSource_unfixed[0]);
					lightSource[1]=Float.parseFloat(lightSource_unfixed[1]);
					lightSource[2]=Float.parseFloat(lightSource_unfixed[2]);
					this.lightPos = new Vector3D(lightSource[0],lightSource[1],lightSource[2]);
			line=input.readLine();
			while(line!=null){
				String[] coordinate_str = line.split("\\s+");
				float[] coordinates=new float[]{
						Float.parseFloat(coordinate_str[0]),
						Float.parseFloat(coordinate_str[1]),
						Float.parseFloat(coordinate_str[2]),
						Float.parseFloat(coordinate_str[3]),
						Float.parseFloat(coordinate_str[4]),
						Float.parseFloat(coordinate_str[5]),
						Float.parseFloat(coordinate_str[6]),
						Float.parseFloat(coordinate_str[7]),
						Float.parseFloat(coordinate_str[8])
				};
                int lightColor[]=new int[]{
                		Integer.parseInt(coordinate_str[9]),
						Integer.parseInt(coordinate_str[10]),
						Integer.parseInt(coordinate_str[11])};
				polygons.add(new Scene.Polygon(coordinates,lightColor));
				line=input.readLine();
			}
				this.scene=new Scene(polygons,lightPos);
	}
		catch(Exception e){
           e.printStackTrace();
		}

//		Vector3D lightSourceVector= new Vector3D(lightSource[0],lightSource[1],lightSource[2]);
//		Scene sc= new Scene(polygons,lightSourceVector);
//        this.scene=sc;
		/*
		 * This method should parse the given file into a Scene object, which
		 * you store and use to render an image.
		 */
	}

	private ArrayList<Scene.Polygon> parseToScene(ArrayList<Float> coordinates, ArrayList<Integer> lightColor) {
		ArrayList<Scene.Polygon> polygons = new ArrayList<>();

		while(!coordinates.isEmpty()){
			float x1=coordinates.remove(0);
			float y1=coordinates.remove(0);
			float z1=coordinates.remove(0);
			Vector3D V1=new Vector3D(x1,y1,z1);
			float x2=coordinates.remove(0);
			float y2=coordinates.remove(0);
			float z2=coordinates.remove(0);
			Vector3D V2=new Vector3D(x2,y2,z2);
			float x3=coordinates.remove(0);
			float y3=coordinates.remove(0);
			float z3=coordinates.remove(0);
			Vector3D V3=new Vector3D(x3,y3,z3);
			int col1 =lightColor.remove(0);
			int col2 = lightColor.remove(0);
			int col3 =lightColor.remove(0);
			Color col = new Color(col1,col2,col3);
			Scene.Polygon polygon=new Scene.Polygon(V1,V2,V3,col);
			polygons.add(polygon);
		}
		return polygons;
	}

	@Override
	protected void onKeyPress(KeyEvent ev) {
		// TODO fill this in.
		/*
		 * This method should be used to rotate the user's viewpoint.
		 */
		if(ev.getKeyCode() == KeyEvent.VK_LEFT||Character.toLowerCase(ev.getKeyChar())=='a'){

  			scene = Pipeline.rotateScene(scene,0,50f);
		}
		else if(ev.getKeyCode() == KeyEvent.VK_RIGHT||Character.toLowerCase(ev.getKeyChar())=='d'){

			scene = Pipeline.rotateScene(scene,0,-50f);
		}
		else if(ev.getKeyCode() == KeyEvent.VK_UP|Character.toLowerCase(ev.getKeyChar())=='w'){

			scene = Pipeline.rotateScene(scene,-50f,0);
		}
		else if(ev.getKeyCode() == KeyEvent.VK_DOWN||Character.toLowerCase(ev.getKeyChar())=='s'){

			scene = Pipeline.rotateScene(scene,50f,0);
		}

	}

	@Override
	protected BufferedImage render() {
		// TODO fill this in.
		/*
		 * This method should put together the pieces of your renderer, as
		 * described in the lecture. This will involve calling each of the
		 * static method stubs in the Pipeline class, which you also need to
		 * fill in.
		 */
		Color[][] bitmap = new Color[CANVAS_WIDTH][CANVAS_HEIGHT];

		// make the bitmap of smoothly changing colors;
		// Your program should render a model
		if(scene==null) return null;
	   scene = Pipeline.translateScene(scene);
	   scene = Pipeline.scaleScene(scene);
	   EdgeList edge;
	   float[][]zdepth = new float[CANVAS_WIDTH][CANVAS_HEIGHT];
		for (int x = 0; x < CANVAS_WIDTH; x++) {
			for (int y = 0; y < CANVAS_HEIGHT; y++) {
				bitmap[x][y] = Color.gray;
				zdepth[x][y] = Float.POSITIVE_INFINITY;
			}
		}
		//System.out.println(scene.getPolygons());
//		for (Scene.Polygon p : scene.getPolygons()){
//			if(!Pipeline.isHidden(p)){//if the polygon is not hidden then draw it
//				Color col= Pipeline.getShading(p,scene.getLight(),Color.white,new Color(getAmbientLight()[0],getAmbientLight()[1],getAmbientLight()[2]));
//				edge= Pipeline.computeEdgeList(p);
//
//				Pipeline.computeZBuffer(bitmap,zdepth,edge,col);
//			}
			for(Scene.Polygon p : scene.getPolygons()){
				EdgeList edgeList= Pipeline.computeEdgeList(p);
				int[] ALcols= getAmbientLight();
				Color lightColor= Color.white;
				Color ALcol=new Color(ALcols[0],ALcols[1],ALcols[2]);

				Color shadingCol=Pipeline.getShading(p,scene.lightSources,lightColor,ALcol);
			   Pipeline.computeZBuffer(bitmap,zdepth,edgeList,shadingCol);
			}


		// render the bitmap to the image so it can be displayed (and saved)
		return convertBitmapToImage(bitmap);
		//Problems here

	}

	@Override
	protected void removeLightSource() {
		scene.removeLightSource();
	}

	@Override
	protected void addLightSource() {
           scene.addLightSource();
	}

	/**
	 * Converts a 2D array of Colors to a BufferedImage. Assumes that bitmap is
	 * indexed by column then row and has imageHeight rows and imageWidth
	 * columns. Note that image.setRGB requires x (col) and y (row) are given in
	 * that order.
	 */
	private BufferedImage convertBitmapToImage(Color[][] bitmap) {
		BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < CANVAS_WIDTH; x++) {
			for (int y = 0; y < CANVAS_HEIGHT; y++) {
				image.setRGB(x, y, bitmap[x][y].getRGB());
			}
		}
		return image;
	}

	public static void main(String[] args) {
		new Renderer();
	}
}

// code for comp261 assignments
