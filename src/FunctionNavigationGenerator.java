
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * cette classe contient un main qui recherche tous les fichiers <nom_table>.table
 *  qui sont dans le méme répertoire que le .jar et pour chaque fichier crée 
 *  un <nom_table>Base.java et <nom_table>BaseService.java. 
 *  Pour chaque morceau de chaque fichier, il y a une méthode é declareé à qui rajoute ce morceau
 * @author dAlonso
 *
 */

public class FunctionNavigationGenerator {
  public static String packageName="FS"; //exemple : GL
  public static String name="FSSLY001_SyntheseEnqueteSLS"; //Exemple : GLTAP606_PanierRubriquesTarification
  public static String suffixe="SYN"; //Exemple : LST (GLTAP606_PanierRubriquesTarificationLST)
  public static String pathOutput = "D:\\Documents and Settings\\gThomazeau\\Mis documentos\\copyPaste\\afaire\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
  public static String pathInput = "D:\\Documents and Settings\\gThomazeau\\Mis documentos\\copyPaste\\afaire\\";
  public static List<ParametreEntree> parametreEntre = null;
  
  public FunctionNavigationGenerator(String pathInput,String pathOutput, String packageName,String name,String suffixe){
    this.packageName=packageName;
    this.pathInput=pathInput;
    this.pathOutput=pathOutput;
    this.name=name;
    this.suffixe=suffixe;
  }
  
  public static void main(String[] argv) throws Exception {
    File dir = new File(pathInput);
    FilenameFilter filterTable = new FilenameFilter() {
        public boolean accept(File dir, String nom) {
          return (nom.startsWith(name) && nom.endsWith(".function"));
        }
      };
    String[] children = dir.list(filterTable);
    
    List<ParametreEntree> dataList=null;
    if (children.length>0){
  //Chaque children est un fichier é partir duquel on veut générer le service de base
    String nomFichier = children[0]; //on prends seulement un fichier
    //for (String nomFichier : children) {
    dataList =recupererParametreEntres();
    }
    //Generate BaseClass
      generateFunctionClass(dataList);
    //}
  }
  
  public static void generateFunctionForRecherche() throws Exception {
    File dir = new File(pathInput);
    FilenameFilter filterTable = new FilenameFilter() {
        public boolean accept(File dir, String nom) {
          return (nom.startsWith(name) && nom.endsWith(".function"));
        }
      };
    String[] children = dir.list(filterTable);
    
    List<ParametreEntree> dataList=null;
    if (children.length>0){
  //Chaque children est un fichier é partir duquel on veut générer le service de base
    String nomFichier = children[0]; //on prends seulement un fichier
    //for (String nomFichier : children) {
    dataList =recupererParametreEntres();
    }
    //Generate BaseClass
      generateFunctionClassForRecherche(dataList);
    //}
  } 
  
  private static List<ParametreEntree> recupererParametreEntres(){
    File dir = new File(pathInput);
    FilenameFilter filterTable = new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return (name.endsWith(".function"));
        }
      };
    String[] children = dir.list(filterTable);
    
    List<ParametreEntree> dataList=null;
  //Chaque children est un fichier é partir duquel on veut générer le service de base
    for (String nomFichier : children) {
      
    /*//Getting Table name
      String tableName = tableNameWithExtension.substring(0,tableNameWithExtension.indexOf(".function"));
    */
      
    //Getting table column, type and mandatory flag
      dataList = gettingDataFromFile(pathInput,nomFichier);
      for (ParametreEntree paramFunction : dataList) {
        System.out.println(paramFunction);
      }
    }
    parametreEntre=dataList;
      return dataList;
  }
  
  public static List<ParametreEntree> getParametreEntre() {
    return parametreEntre;
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
    sb.append(" *     <TD><I>Modifié le :</I></TD>\n");
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
  
  private static String declareFunctionClassDeclarationForRecherche() {
    StringBuilder sb = new StringBuilder();
    sb.append("@NavigationFunction(\""+packageName+"/"+name+suffixe+ "\") \n");
    //sb.append("public class ").append(name+suffixe).append("Function extends AbstractFunctionCall {\n");
    sb.append("public class ").append(name+suffixe).append("Function extends AbstractInOutSearchFunctionCall {\n");
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
        if (!str.trim().isEmpty()){
          ParametreEntree data = new ParametreEntree(str);
          dataList.add(data);
        }
      } 
      in.close(); 
      
    } catch (FileNotFoundException e) {e.printStackTrace(); } 
    catch (IOException ioe){}
    return dataList;      
  }

  public static void generateFunctionClass(List<ParametreEntree> dataList){
    try {
      File file = new File(pathOutput+name+suffixe+"Function.java");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
      out.append(declarePackage(packageName));
      out.append(declareImportFunctionClass(dataList));
      out.append(declareFunctionClassCommentary());
      out.append(declareFunctionClassDeclaration());
      if (dataList!=null){
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
      }
      out.append("}//fin classe ").append(name+suffixe).append("Function");
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public static void generateFunctionClassForRecherche(List<ParametreEntree> dataList){
    try {
      File file = new File(pathOutput+name+suffixe+"Function.java");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
      out.append(declarePackage(packageName));
      out.append(declareImportFunctionClass(dataList));
      out.append(declareFunctionClassCommentary());
      out.append(declareFunctionClassDeclarationForRecherche());
      if (dataList!=null){
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
    sb.append("import sopra.navigation.AbstractFunctionCall;\n");
    sb.append("import sopra.navigation.NavigationFunction;\n");
    sb.append("import java.util.List;\n");
    if(contains(dataList,"BigDecimal"))
      sb.append("import java.math.BigDecimal;\n");
    if(contains(dataList,"Date"))
      sb.append("import java.util.Date;\n");
    System.out.println("Don't forget to open the result file on eclipse and check import.");
    return sb.toString();   
  }

  private static String declarePackage(String packageName) {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n").append("* Paquet de définition\n").append("*/\n");
    sb.append("package sopra.immobilier.navigation."+packageName+"; \n\n");
    return sb.toString();
  }
  
  private static boolean contains(List<ParametreEntree> dataList, String type){
    boolean found = false;
    if (dataList!=null){
      Iterator<ParametreEntree> it = dataList.iterator();
      while (it.hasNext()&&(!found)) {
        ParametreEntree dataTypeMandatory = it.next();
        found = dataTypeMandatory.getType().equalsIgnoreCase(type);
      }
    }
    return found;
    
  }

  
}
