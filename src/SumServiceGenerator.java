
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
 * @author dAlonso
 *
 */

public class SumServiceGenerator {
  public static String packageName="GL"; //exemple : GL
  public static String name="GLTAR018_RechercheGroupeDePrix"; //Exemple : GLTAP606_PanierRubriquesTarification
  public static String suffixe="LST"; //Exemple : LST (GLTAP606_PanierRubriquesTarificationLST)
  public static String pathOutput = "D:\\Ulis\\generateur\\afaire\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
  public static String pathInput = "D:\\Ulis\\generateur\\afaire\\";
  public static List<Proxy> listeProxy = null;
  
  public SumServiceGenerator(String pathInput,String pathOutput, String packageName,String name,String suffixe,List<Proxy> listeProxy){
    this.packageName=packageName;
    this.pathInput=pathInput;
    this.pathOutput=pathOutput;
    this.name=name;
    this.suffixe=suffixe;
    this.listeProxy=listeProxy;
  }
  
  public static void main(String[] argv) throws Exception {
    
    //Generate BaseClass
      generateSumService();
    //}
  }
  
  public static void generateSumService() throws Exception {
    try {
      File file = new File(pathOutput+name+"SumService.java");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
      out.append(declarePackage(packageName));
      out.append(declareImportFunctionClass());
      out.append(declareFunctionClassCommentary());
      out.append(declareSumServiceClassDeclaration());
      out.append(declareProcessFindByCriteria(listeProxy));
      out.append("}//fin classe ").append(name).append("SumService");
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    
    try {
      File file = new File(pathOutput+name+"SumBean.java");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
      out.append(declarePackage(packageName));
      out.append(declareImportBeanClass());
      out.append(declareBeanClassCommentary());
      out.append(declareSumBeanClassDeclaration());
      out.append("}//fin classe ").append(name).append("SumService");
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
  
  private static String declareBeanClassCommentary() {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append("/**\n");
    sb.append(" * Description :\n");
    sb.append(" * Donnez une description textuelle du service fourni par cette classe.\n");
    sb.append(" *\n");
    sb.append(" * <TABLE>\n");
    sb.append("*   <TR>\n");
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
    sb.append(" * @author  : SOPRA Group - $Author:   gThomazeau  $\n");
    sb.append(" *\n");
    sb.append(" */\n");
    return sb.toString();
  }

  private static String declareSumServiceClassDeclaration() {
    StringBuilder sb = new StringBuilder();
    sb.append("public class "+name+"SumService extends AbstractULISSQLService{\n");
    sb.append("  \n");
    sb.append("  /** Version extraite du gestionnaire de configuration */\n");
    sb.append("  @SuppressWarnings(\"hiding\")\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision:   1.0  $\";\n");
    sb.append("  /** Nom logique du service */\n");
    sb.append("  public static final String SERVICE_NAME = \"ULIS.referentiel."+packageName+"."+name+"SumService\";\n");
    sb.append("  /** Description textuelle du service */\n");
    sb.append("  public static final String SERVICE_DESCRIPTION = \"Service de simulation de somme \";  \n");
    sb.append("  \n");
    sb.append("  /**\n");
    sb.append("   * Créer une service somme pour "+name+"\n");
    sb.append("   */\n");
    sb.append("  public "+name+"SumService() {\n");
    sb.append("    super();\n");
    sb.append("  }\n");
    sb.append("  \n");
    sb.append("  /**\n");
    sb.append("   * @see sopra.immobilier.service.business.Service#getDescription()\n");
    sb.append("   */\n");
    sb.append("  @Override\n");
    sb.append("  public String getDescription() {\n");
    sb.append("    return SERVICE_DESCRIPTION;\n");
    sb.append("  }\n");
    sb.append("  \n");
    sb.append("  /**\n");
    sb.append("   * @see sopra.immobilier.service.business.Service#getName()\n");
    sb.append("   */\n");
    sb.append("  @Override\n");
    sb.append("  public String getName() {\n");
    sb.append("    return SERVICE_NAME;\n");
    sb.append("  }\n");
    sb.append(" \n");
    sb.append(" /**\n");
    sb.append("  * @see sopra.immobilier.service.business.Service#getVersion()\n");
    sb.append("  */\n");
    sb.append("  @Override\n");
    sb.append("  public String getVersion() {\n");
    sb.append("    return CLASS_VERSION;\n");
    sb.append("  }  \n");
    return sb.toString();
  }
  
  
  
  public static String declareProcessFindByCriteria(List<Proxy> dataList){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Trouver des sommes pour les résultats de \n");
    sb.append(" * {@link sopra.immobilier.business.ULIS.referentiel."+packageName+"."+name+"#processFindByCriteria}\n");
    sb.append(" * @param cnx Connection\n");
    sb.append(" * @param context ServiceContext\n");
    sb.append(" * @param criteria ServiceCriteria\n");
    sb.append(" * @param findAttributes ServiceFindAttributes\n");
    sb.append(" * @return ServiceResponse\n");
    sb.append(" * @throws ServiceException Exception\n");
    sb.append(" * @throws ServiceObjectNotFoundException Exception\n");
    sb.append(" */\n");
    sb.append(" @Override\n");
    sb.append("protected ServiceResponse processFindByCriteria(Connection cnx,\n");
    sb.append("                                                ServiceContext context,\n");
    sb.append("                                                ServiceCriteria criteria,\n");
    sb.append("                                                ServiceFindAttributes findAttributes)\n");
    sb.append("throws ServiceException, ServiceObjectNotFoundException{    \n");
    sb.append("   \n");
    sb.append("  ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
    sb.append("  \n");
    sb.append("  "+name+"SumBean bean = new "+name+"SumBean();\n");
    sb.append("  \n");
    sb.append("  try{\n");
    sb.append("    \n");
    sb.append("    ServiceResponse response = resolveServiceResponseByCriteria("+name+"Service.SERVICE_NAME,\n");
    sb.append("                                                                context, \n");
    sb.append("                                                                criteria);\n");
    sb.append("                                                                \n");
    sb.append("    List<"+name+"Proxy> responseList = \n");
    sb.append("     (List<"+name+"Proxy>) response.getElementsAsList();\n");
    sb.append("     \n");
    sb.append("    int iter=0;\n");
    sb.append("    \n");
    for (Proxy proxy : dataList) {
      if (proxy.getType().equalsIgnoreCase("BigDecimal") && proxy.isSommable()){
        sb.append("    bean.set"+proxy.getNom()+"(new BigDecimal(0));\n");
      }
    }
    
    
    sb.append("    \n");
    sb.append("    while(iter<responseList.size()){\n");       
    for (Proxy proxy : dataList) {
      if (proxy.getType().equalsIgnoreCase("BigDecimal") && proxy.isSommable()){
        sb.append("     if(responseList.get(iter).get"+proxy.getNom()+"()!=null)\n");
        sb.append("        bean.set"+proxy.getNom()+"(bean.get"+proxy.getNom()+"().add(responseList.get(iter).get"+proxy.getNom()+"()));   \n");    
      }
    }
    
    sb.append("        \n");
    sb.append("      iter=iter+1;\n");
    sb.append("      \n");
    sb.append("    }\n");
    sb.append("  }catch(ServiceObjectNotFoundException e){\n");
    sb.append("    throw e;\n");
    sb.append("  }catch(ServiceException  e){\n");
    sb.append("    throw e;\n");
    sb.append("  }\n");
    sb.append("  serviceResponse = addElement(serviceResponse, bean);\n");
      
    sb.append("  return serviceResponse;\n");
    sb.append("}  \n");
    return sb.toString();
  }

   
  private static String declareImportFunctionClass() {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append("* Paquet de définition\n");
    sb.append(" */\n");
    sb.append("import java.math.BigDecimal;\n");
    sb.append("import java.sql.Connection;\n");
    sb.append("import java.util.List;\n");
    sb.append("import sopra.immobilier.business.ULIS.AbstractULISSQLService;\n");
    sb.append("import sopra.immobilier.service.business.ServiceContext;\n");
    sb.append("import sopra.immobilier.service.business.ServiceCriteria;\n");
    sb.append("import sopra.immobilier.service.business.ServiceException;\n");
    sb.append("import sopra.immobilier.service.business.ServiceFindAttributes;\n");
    sb.append("import sopra.immobilier.service.business.ServiceObjectNotFoundException;\n");
    sb.append("import sopra.immobilier.service.business.ServiceResponse;\n");
    return sb.toString();   
  }
  
  private static String declareImportBeanClass() {
    StringBuilder sb = new StringBuilder();
    sb.append("import java.math.BigDecimal;\n");
    sb.append("import sopra.immobilier.business.ULIS.AbstractBusinessObject;\n");
    sb.append("import sopra.immobilier.model.ModelBean;\n");
    sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForAnAmount;\n");
    return sb.toString();   
  }

  private static String declareSumBeanClassDeclaration() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("@ModelBean\n");
    sb.append("public class "+name+"SumBean extends AbstractBusinessObject{\n");
    sb.append("  \n");
    sb.append("  /** Version extraite du gestionnaire de configuration */\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision:   1.0  $\";\n");
    sb.append("  \n");
    sb.append("  /** Somme pour les montants dans {@link sopra.immobilier.business.ULIS.referentiel."+packageName+"."+name+"Proxy}*/\n");
    
    sb.append("/**\n");
    sb.append(" * Constructeur\n");
    sb.append(" */\n");
    sb.append("public "+name+"SumBean() {\n");
    sb.append("  super();\n");
    sb.append("}\n");
    
    //declaration des constantes
    for (Proxy proxy : listeProxy) {
      if (proxy.getType().equalsIgnoreCase("BigDecimal") && proxy.isSommable()){
        sb.append("/** "+proxy.getDescription()+" */\n");
        sb.append("private BigDecimal "+proxy.getNom()+" = null;  \n");
      }
    }
    
  //declaration des getter et setter
    for (Proxy proxy : listeProxy) {
      if (proxy.getType().equalsIgnoreCase("BigDecimal") && proxy.isSommable()){
        
        sb.append("/**\n");
        sb.append(" * @return la valeur positionnée au sein de la propriété\n");
        sb.append(" */\n");
        sb.append(" @ForAnAmount(digits="+proxy.getLongueur()+",fractionDigit="+proxy.getLongueurFraction()+")\n");
        sb.append(" public BigDecimal get"+proxy.getNom()+"() {\n");
        sb.append("  return "+proxy.getNom()+";\n");
        sb.append("}\n");
        sb.append("\n");
        sb.append("/**\n");
        sb.append("  * @param "+proxy.getNom()+" valeur à positionner au sein de la propriété\n");
        sb.append("  */\n");
        sb.append(" public void set"+proxy.getNom()+"(BigDecimal "+proxy.getNom()+") {\n");
        sb.append("   if (isLoggingDebug())\n");
        sb.append("     logDebug(\"set"+proxy.getNom()+"() <-- \" + "+proxy.getNom()+");\n");
        sb.append("   //\n");
        sb.append("   this."+proxy.getNom()+" = "+proxy.getNom()+";\n");
        sb.append("}\n");
       
      }
    }    
    
    
    return sb.toString();
  }
  
  private static String declarePackage(String packageName) {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n").append("* Paquet de définition\n").append("*/\n");
    sb.append("package sopra.immobilier.business.ULIS."+packageName+"; \n\n");
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
