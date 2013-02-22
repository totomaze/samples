
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

/**
 * cette classe contient un main qui recherche tous les fichiers <nom_table>.table
 *  qui sont dans le méme répertoire que le .jar et pour chaque fichier crée 
 *  un <nom_table>Base.java et <nom_table>BaseService.java. 
 *  Pour chaque morceau de chaque fichier, il y a une méthode é declareé à qui rajoute ce morceau
 * @author gth
 *
 */

public class ExportServiceGenerator {
  public static String packageName="GL"; //exemple : GL
  public static String name="GLTAR018_RechercheGroupeDePrix"; //Exemple : GLTAP606_PanierRubriquesTarification
  public static String suffixe="LST"; //Exemple : LST (GLTAP606_PanierRubriquesTarificationLST)
  public static String pathOutput = "D:\\Ulis\\generateur\\afaire\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
  public static String pathInput = "D:\\Ulis\\generateur\\afaire\\";
  public static List<Proxy> listeProxy = null;
  public static String codeTable = null;
  public static String code = null;
  public static List<String> listeChampExport=null;
  
  public ExportServiceGenerator(String pathInput,String pathOutput, String packageName,String name,String suffixe,List<Proxy> listeProxy,TypeExport export){
    this.packageName=packageName;
    this.pathInput=pathInput;
    this.pathOutput=pathOutput;
    this.name=name;
    this.suffixe=suffixe;
    this.listeProxy=listeProxy;
    this.codeTable=export.getCodeTable();
    this.code=name.substring(0, name.indexOf("_"));
    this.listeChampExport=export.getListeChampExport();
  }
  
  public static void main(String[] argv) throws Exception {
    
    //Generate BaseClass
      generateExportService();
    //}
  }
  
  public static void generateExportService() throws Exception {
    
    try {
      File file = new File(pathOutput+"ExportBI"+code+"CreateService.java");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
      out.append(declarePackage(packageName));
      out.append(declareImportClass());
      out.append(declareClassCommentary());
      out.append(declareExportBIClassDeclaration());
      out.append(declareExportBIServiceParametersClassDeclaration());
      
      out.append("}//fin classe ").append(name).append("Export");
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  


  private static String declareFunctionClassCommentary() {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append("/**\n");
    sb.append(" * Description :\n");
    sb.append(" * Donnez une description textuelle du service fourni par cette classe.\n");
    sb.append(" *\n");
    sb.append(" * <TABLE>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
    sb.append(" *     <TD>&nbsp;</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
    sb.append(" *     <TD>$HeadURL:   O:/Kernel5 Livraison/archives/kernel/tools/eclipse/config/ArchitectureImmobilierCodeTemplates.xml-arc  $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Modifié le :</I></TD>\n");
    sb.append(" *     <TD>$Date:   Apr 16 2009 11:52:40  $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append("* </TABLE>\n");
    sb.append("*\n");
    sb.append("* @version : $Revision:   1.0  $\n");
    sb.append("* @author  : SOPRA Group - $Author:   Gthomzeau  $\n");
    sb.append("*\n");
    sb.append("*/\n");
    return sb.toString();
  }
  
  private static String declareClassCommentary() {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Description :\n");
    sb.append(" * Donnez une description textuelle du service fourni par cette classe.\n");
    sb.append(" *\n");
    sb.append(" * <TABLE>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
    sb.append(" *     <TD>&nbsp;</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
    sb.append(" *     <TD>$HeadURL:   O:/Kernel5 Livraison/archives/kernel/tools/eclipse/config/ArchitectureImmobilierCodeTemplates.xml-arc  $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Modifié le :</I></TD>\n");
    sb.append(" *     <TD>$Date:   Apr 16 2009 11:52:40  $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" * </TABLE>\n");
    sb.append(" *\n");
    sb.append(" * @version : $Revision:   1.0  $\n");
    sb.append(" * @author  : SOPRA Group - $Author:   tintin  $\n");
    sb.append(" *\n");
    sb.append(" */\n");
    return sb.toString();
  }

  
   
  
  private static String declareImportClass() {
    StringBuilder sb = new StringBuilder();
    sb.append("import java.sql.Connection;\n");
    sb.append("import java.sql.PreparedStatement;\n");
    sb.append("import java.sql.ResultSet;\n");
    sb.append("import java.sql.SQLException;\n");
    sb.append("import java.util.ArrayList;\n");
    sb.append("import java.util.List;\n");
    sb.append("import sopra.immobilier.business.ULIS.referentiel.AbstractLanceurExportBIService;\n");
    sb.append("import sopra.immobilier.service.business.ServiceContext;\n");
    sb.append("import sopra.immobilier.service.business.ServiceException;\n");
    sb.append("import sopra.immobilier.service.business.ServiceParameters;\n");
    return sb.toString();   
  }

  private static String declareExportBIClassDeclaration() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("public class ExportBI"+code+"CreateService extends AbstractLanceurExportBIService {\n");
    sb.append("  \n");
    sb.append("  /** Version extraite du gestionnaire de configuration */\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision:   1.0  $\";\n");
    sb.append("  /** Nom logique du service */\n");
    sb.append("  public static final String SERVICE_NAME = \"ULIS.referentiel."+packageName+".ExportBI"+code+"CreateService\";\n");
    sb.append("  /** Description textuelle du service */\n");
    sb.append("   public static final String SERVICE_DESCRIPTION = \"Service permettant de déclencher, depuis le web, un export bureautique des operations\";\n");
    sb.append("  \n");
    sb.append("  /**\n");
    sb.append("   * Retourne le nom du service métier\n");
    sb.append("   * @return le nom du service métier\n");
    sb.append("   */\n");
    sb.append("  @Override\n");
    sb.append("  public String getName() {\n");
    sb.append("    return SERVICE_NAME;\n");
    sb.append("  }\n");
    sb.append("  /**\n");
    sb.append("   * Retourne une description textuelle du service métier\n");
    sb.append("   * @return la description textuelle du service\n");
    sb.append("   */\n");
    sb.append("  @Override\n");
    sb.append("  public String getDescription() {\n");
    sb.append("    return SERVICE_DESCRIPTION;\n");
    sb.append("  }\n");
    sb.append("  /**\n");
    sb.append("   * Retourne la version du service métier sous forme de chaîne de caractères\n");
    sb.append("   * @return la version du service métier\n");
    sb.append("   */\n");
    sb.append("  @Override\n");
    sb.append("  public String getVersion() {\n");
    sb.append("    return CLASS_VERSION;\n");
    sb.append("  }\n");
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.business.ULIS.referentiel.AbstractLanceurExportBIService#getBIDTP_NUM()\n");
    sb.append(" */\n");
    sb.append("@Override\n");
    sb.append("protected String getBIDTP_NUM() {\n");
    sb.append("  return \"FSSLI024\";\n");
    sb.append("}\n");
    sb.append("\n");
    sb.append(" /**\n");
    sb.append(" * @see sopra.immobilier.business.ULIS.referentiel.AbstractLanceurExportBIService#getWORK_TABLE_NAME()\n");
    sb.append(" */\n");
    sb.append(" @Override\n");
    sb.append(" protected String getWORK_TABLE_NAME() {\n");
    sb.append("   return \""+codeTable+"\";\n");
    sb.append(" }  \n");
    
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.business.ULIS.referentiel.AbstractLanceurExportBIService#computeEditionParameterList(sopra.immobilier.service.business.ServiceParameters, sopra.immobilier.service.business.ServiceContext, java.sql.Connection)\n");
    sb.append(" * Méthode permettant de générer la liste des paramètres à insérer dans la table\n");
    sb.append(" */\n");
    sb.append(" @Override\n");
    sb.append(" protected List<String> computeEditionParameterList(ServiceParameters param,\n");
    sb.append("                                                   ServiceContext context,\n");
    sb.append("                                                   Connection cnx)\n");
    sb.append("                                                   throws ServiceException {\n");
    sb.append("   \n");
    sb.append("  ExportBI"+code+"CreateServiceParameters serviceParameters = new ExportBI"+code+"CreateServiceParameters(param);\n");
    sb.append("  \n");
    sb.append(" //on constitue la liste des paramètres attendus pour la génération du document\n");
    sb.append(" List<String> listeParam = new ArrayList<String>();\n");
    sb.append(" listeParam.add(serviceParameters.get"+codeTable.toUpperCase()+"_CPT().toString());\n");
    sb.append(" \n");
    sb.append("  return listeParam;\n");
    sb.append(" }\n");
    return sb.toString();
  }
  
  private static String declareExportBIServiceParametersClassDeclaration() {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Description :\n");
    sb.append("* Donnez une description textuelle du service fourni par cette classe.\n");
    sb.append("*\n");
    sb.append("* <TABLE>\n");
    sb.append("*   <TR>\n");
    sb.append("*     <TD><B>Attributs SVN :</B></TD>\n");
    sb.append("*     <TD>&nbsp;</TD>\n");
    sb.append("*   </TR>\n");
    sb.append("*   <TR>\n");
    sb.append("*     <TD><I>Nom du fichier :</I></TD>\n");
    sb.append("*     <TD>$HeadURL: http://svn.lyon.fr.sopra/svnulis/svn/ulis_test/repository/Refonte/ULISNG/ULISNG-MODULES/branches/ULISNG-N1-LOTA-GLCO-AC-CA-4/src/main/java/sopra/immobilier/business/ULIS/referentiel/OP/ExportBIESIStockCreateService.java $</TD>\n");
    sb.append("*   </TR>\n");
    sb.append("*   <TR>\n");
    sb.append("*     <TD><I>Modifié le :</I></TD>\n");
    sb.append("*     <TD>$Date: 2011-05-16 13:57:05 +0200 (lun, 16 may 2011) $</TD>\n");
    sb.append("*   </TR>\n");
    sb.append("* </TABLE>\n");
    sb.append("*\n");
    sb.append("* @version : $Revision: 25653 $\n");
    sb.append("* @author  : SOPRA Group - $Author: gthomazeau $\n");
    sb.append("*\n");
    sb.append("*/\n");
    sb.append("public static class ExportBI"+code+"CreateServiceParameters extends AbstractLanceurExportBIServiceParameters {\n");
    sb.append("\n");
    sb.append("/** Version extraite du gestionnaire de configuration */\n");
    sb.append("@SuppressWarnings(\"hiding\")\n");
    sb.append("public static final String CLASS_VERSION = \"$Revision: 25653 $\";    \n");
    sb.append("\n");
    sb.append("/** Nom du critère donnant accès au compteur */\n");
    sb.append("protected static final String "+codeTable.toUpperCase()+"_CPT_PARAMETER_NAME = \""+codeTable.toUpperCase()+"_CPT\";\n");
    sb.append("\n");
    sb.append("/**\n");
    sb.append("* @return la valeur positionnée au sein de la propriété\n");
    sb.append("*/\n");
    sb.append("public String get"+codeTable.toUpperCase()+"_CPT_PARAMETER_NAME() {\n");
    sb.append("return "+codeTable.toUpperCase()+"_CPT_PARAMETER_NAME;\n");
    sb.append("}    \n");
    sb.append("\n");
    sb.append("/**\n");
    sb.append("* @return la valeur positionnée au sein de la propriété\n");
    sb.append("*/    \n");
    sb.append("public Integer get"+codeTable.toUpperCase()+"_CPT() {\n");
    sb.append("return getParameterValueAsInteger("+codeTable.toUpperCase()+"_CPT_PARAMETER_NAME);\n");
    sb.append("}\n");
    sb.append("\n");
    sb.append("/**\n");
    sb.append("* Crée une nouvelle façade sur les paramètre permettant  de lancer une édition\n");
    sb.append("* @param param paramètre génériques devant être masqués par cette façade\n");
    sb.append("*/\n");
    sb.append("protected ExportBI"+code+"CreateServiceParameters(ServiceParameters param) {\n");
    sb.append("super(param);\n");
    sb.append("}    \n");
    sb.append("} \n");
    sb.append("\n");
    sb.append("/**\n");
    sb.append("* La liste des propriété à ne pas exporté bien qu'elles soient affichées.\n");
    sb.append("* Doit être surchargée par le développeur s'il souhaite filtrer les donnée à exporter\n");
    sb.append("* @return La liste des propriété à ne pas exporté bien qu'elles soient affichées\n");
    sb.append("*/\n");
    sb.append("@Override\n");
    sb.append("protected List<String> getParameterListToFilter() {\n");
    sb.append("\n");
    sb.append("      //retourne la liste des noms des données métier à ne pas exporter malgré leur affichage    \n");
    sb.append("List<String> listeFilter = new ArrayList<String>();\n");
    for(Proxy proxy:listeProxy){
      if (listeChampExport!=null && listeChampExport.size()>0){
      for(String champExport:listeChampExport){
        if (!listeChampExport.contains(proxy.getNom())){
          sb.append("listeFilter.add(\""+proxy.getNom()+"\");\n");
        }
      }
      }else{
        sb.append("\\supprimer les champs present dans l'export\n");
        sb.append("listeFilter.add(\""+proxy.getNom()+"\");\n");
      }
    }
    sb.append("listeFilter.add(\"YYY\");\n");
    sb.append("\n");
    sb.append("return listeFilter;\n");
    sb.append("}\n");
    sb.append("\n");
    sb.append("/**\n");
    sb.append("* Point d'entrée développeur permettant de mettre à jour une donnée venant d'être inséré dans la table de travail.\n");
    sb.append("* @param cnx la connexion\n");
    sb.append("* @param data la donnée qui vient d'être inséré\n");
    sb.append("* @param profileID l'identifiant du profil\n");
    sb.append("* @param cptVal le compteur de la table de travail\n");
    sb.append("* @throws ServiceException en cas d'erreur technique\n");
    sb.append("*/\n");
    sb.append("@Override\n");
    sb.append("protected void completeInsertedData(Connection cnx, \n");
    sb.append("Object data,\n");
    sb.append("String profileID,\n");
    sb.append("ServiceContext context, \n");
    sb.append("int cptVal,Integer rowNum)\n");
    sb.append("throws ServiceException {\n");
    sb.append("\n");
    sb.append("PreparedStatement stmt = null;\n");
    sb.append("ResultSet rs = null;\n");
    sb.append(name+"Proxy proxy = ("+name+"Proxy) data;\n");
    sb.append("\n");
    sb.append("String sql = \"\";\n");
    sb.append("\n");
    sb.append("try{\n");
    sb.append("\n");
    sb.append("if(!sql.equals(\"\")){\n");
    sb.append("String update_statut = \"UPDATE "+codeTable+" SET \" + sql +\n");
    int cpt=0;
    for (String champExport:listeChampExport){
      if (cpt>0){sb.append(" , ");}
      sb.append("\" AND "+champExport+" = \"+ proxy.get"+champExport+"() +\" \" +\n");
      cpt++;
    }
    sb.append("\" WHERE mguti_cod = '\"+ profileID +\"' \" +\n");
    sb.append("\" AND "+codeTable+"_NUM = \"+ rowNum +\n");
    sb.append("\" AND "+codeTable+"_CPT = \"+ cptVal;\n");
    sb.append("\n");
    sb.append("stmt = cnx.prepareStatement(update_statut);\n");
    sb.append("rs = stmt.executeQuery();\n");
    sb.append("}\n");
    sb.append("} \n");
    sb.append("catch (SQLException databaseEx) {\n");
    sb.append("        // Exception soulevée par l'accès à la couche de persistance\n");
    sb.append("ServiceException ioEx = new ServiceException(\"Fatal error while accessing to persistence layer\",\n");
    sb.append("databaseEx);\n");
    sb.append("if (isLoggingError()) {\n");
    sb.append("logError(databaseEx);\n");
    sb.append("logError(ioEx);\n");
    sb.append("}\n");
    sb.append("throw ioEx;     \n");   
    sb.append("}\n");
    sb.append("finally {\n");
    sb.append("        // On ferme l'ensemble des ressources que nous avons ouvertes au sein \n");
    sb.append("        // de la base de données\n");
    sb.append("if (rs != null) {\n");
    sb.append("try {\n");
    sb.append("rs.close();\n");
    sb.append("}\n");
    sb.append("catch (SQLException ignoreEx) {}\n");
    sb.append("rs = null;\n");
    sb.append("}\n");
    sb.append("        //\n");
    sb.append("if (stmt != null) {\n");
    sb.append("try {\n");
    sb.append("stmt.close();\n");
    sb.append("}\n");
    sb.append("catch (SQLException ignoreEx) {}\n");
    sb.append("stmt = null;\n");
    sb.append("}\n");
    sb.append("}   \n"); 
    sb.append("}\n");
    return sb.toString();
  }
  
  private static String declarePackage(String packageName) {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n").append("* Paquet de définition\n").append("*/\n");
    sb.append("package sopra.immobilier.business.ULIS.referentiel."+packageName+"; \n\n");
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
