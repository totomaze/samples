
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * cette classe contient un main qui recherche tous les fichiers <nom_table>.table
 *  qui sont dans le m�me r�pertoire que le .jar et pour chaque fichier cr�e 
 *  un <nom_table>Base.java et <nom_table>BaseService.java. 
 *  Pour chaque morceau de chaque fichier, il y a une m�thode � declare� � qui rajoute ce morceau
 * @author dAlonso
 *
 */

public class PanierGenerator {
  public static String packageName="GL"; //exemple : GL
  public static String name="GLTAP609_PanierNatureDeDepense"; //Exemple : GLTAP606_PanierRubriquesTarification
  public static String suffixe="LST"; //Exemple : LST (GLTAP606_PanierRubriquesTarificationLST)
  public static String pathOutput = "D:\\Ulis\\generateur\\afaire\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
  public static String pathInput = "D:\\Ulis\\generateur\\afaire\\";
  
  
  public PanierGenerator(String path, String packageName,String name,String suffixe){
    this.packageName=packageName;
    this.pathOutput=path;
    this.name=name;
    this.suffixe=packageName;
  }
  
  public static void main(String[] argv) throws Exception {
    FunctionNavigationGenerator function= new FunctionNavigationGenerator(pathInput,pathOutput,packageName, name, suffixe);
    FunctionNavigationGenerator.main(null);
    ServiceGenerator serviceGenerator= new ServiceGenerator(pathInput,pathOutput, packageName, name, suffixe);
    ServiceGenerator.main(null);
    ScreenControllerGenerator screenController= new ScreenControllerGenerator(FunctionNavigationGenerator.getParametreEntre(),serviceGenerator.getListeCriteria(),serviceGenerator.getListeProxy(), pathOutput, packageName, name, suffixe);
    ScreenControllerGenerator.generateScreenControllerClassForPanier();
    XHTMLGenerator xhmlGenerator= new XHTMLGenerator(pathInput,pathOutput, packageName, name, suffixe,ServiceGenerator.getListeProxy(),ServiceGenerator.getListeCriteria());
    XHTMLGenerator.generateXHTMLForPanier();
  }
  
 
  private static String declareFunctionClassCommentary() {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Description :\n");
    sb.append(" * fonction de navigation pour la page ").append(name+suffixe).append("\n");
    sb.append(" *\n");
    sb.append(" * <TABLE>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
    sb.append(" *     <TD>&nbsp;</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
    sb.append(" *     <TD>$HeadURL: \n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Modifi� le :</I></TD>\n");
    sb.append(" *     <TD>$Date: 2010-10-18 18:14:44 +0200 (lun, 18 oct 2010) $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" * </TABLE>\n");
    sb.append(" *\n");
    sb.append(" * @version : $Revision: 1 $\n");
    sb.append(" * @author  : SOPRA Group - $Author: gthomazeau $\n");
    sb.append(" *\n");
    sb.append(" */\n");
    return sb.toString();
  }

  private static String declareFunctionClassDeclaration() {
    StringBuilder sb = new StringBuilder();
    sb.append("@NavigationFunction(\""+packageName+"/"+name+suffixe+ "\") \n");
    sb.append("public class ").append(name+suffixe).append("Function extends AbstractFunctionCall {\n");
    sb.append("  //Constantes\n");
    sb.append("  /** Version extraite du gestionnaire de configuration */\n");
    sb.append("  @SuppressWarnings(\"hiding\")\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision: $\";\n");
    return sb.toString();
  }

  private static ArrayList<ParametreEntree> gettingDataFromFile(String path,String nomFichier) {
    ArrayList<ParametreEntree> dataList = new ArrayList<ParametreEntree>();
    try { 
      BufferedReader in = new BufferedReader(new FileReader(path+nomFichier)); 
      String str; 
      
      while ((str = in.readLine()) != null) { 
        ParametreEntree data = new ParametreEntree(str);
        dataList.add(data);
      } 
      in.close(); 
      
    } catch (FileNotFoundException e) {e.printStackTrace(); } 
    catch (IOException ioe){}
    return dataList;      
  }

  private static void generateFunctionClass(String path,String pck, String name,String suffixe, List<ParametreEntree> dataList){
    
    try {
      File file = new File(path+name+suffixe+"Function.java");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
      out.append(declarePackage(pck));
      out.append(declareImportFunctionClass(dataList));
      out.append(declareFunctionClassCommentary());
      out.append(declareFunctionClassDeclaration());
      for (ParametreEntree paramFunction : dataList) {
        out.append(paramFunction.declareConstante());
        out.append("\n");       
      }
      for (ParametreEntree paramFunction : dataList) {
        if (paramFunction.isEntree()){
          out.append(paramFunction.declareINGetter());
          out.append(paramFunction.declareINSetter());
        }
        
        if (paramFunction.isSortie()){
          out.append(paramFunction.declareOUTGetter());
          out.append(paramFunction.declareOUTSetter());
        }
        
        out.append("\n");       
      }
      out.append("}//fin classe ").append(name+suffixe).append("Function");
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }

  private static String declareClasseDeclaration(String tableName) {
    StringBuilder sb = new StringBuilder();
    sb.append("public class ").append(tableName).append("Base extends AbstractBusinessObject {\n");
    sb.append("  //Constantes\n");
    sb.append("  /** Version extraite du gestionnaire de configuration */\n");
    sb.append("  @SuppressWarnings(\"hiding\")\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision: $\";\n");
    sb.append("  //Attributs\n");
    return sb.toString();
  }

  
  private static String declareImportFunctionClass(List<ParametreEntree> dataList) {
    StringBuilder sb = new StringBuilder();
    sb.append("\nimport sopra.immobilier.business.ULIS.AbstractBusinessObject;\n");
    if(contains(dataList,"BigDecimal"))
      sb.append("import java.math.BigDecimal;\n");
    if(contains(dataList,"Date"))
      sb.append("import java.util.Date;\n");
    System.out.println("Don't forget to open the result file on eclipse and check import.");
    return sb.toString();   
  }

  private static String declarePackage(String packageName) {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n").append("* Paquet de d�finition\n").append("*/\n");
    sb.append("package sopra.immobilier.navigation."+packageName+"; \n\n");
    return sb.toString();
  }
  
  private static boolean contains(List<ParametreEntree> dataList, String type){
    boolean found = false;
    Iterator<ParametreEntree> it = dataList.iterator();
    while (it.hasNext()&&(!found)) {
      ParametreEntree dataTypeMandatory = it.next();
      found = dataTypeMandatory.getType().equalsIgnoreCase(type);
    }
    return found;
    
  }

  
}
