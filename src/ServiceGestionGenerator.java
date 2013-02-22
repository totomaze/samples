
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
 *  Pour chaque morceau de chaque fichier, il y a une méthode é declareé é qui rajoute ce morceau
 * @author Gthomazeau
 *
 */

public class ServiceGestionGenerator {
  public static String packageName="FS"; //exemple : GL
  public static String name="FSSLY001_CadreIdentifEnquêteSLS"; //Exemple : GLTAP606_PanierRubriquesTarification
  public static String suffixe=""; //Exemple : LST (GLTAP606_PanierRubriquesTarificationLST)
  public static String pathOutput = "D:\\Ulis\\generateur\\BaseServiceGenerator\\src\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
  public static String pathInput = "D:\\Ulis\\generateur\\BaseServiceGenerator\\src\\";
  public static List<Proxy> listeProxy =null;
  public static List<Criteria> listeCriteria =null;
  public static List<TypeCompute> listeCompute =null;
  public static List<String> listeComposantMetier =null;
  private static boolean traitementSynthese=false;
  private static boolean multiOccurence=false;
  
  public static List<Proxy> getListeProxy() {
    return listeProxy;
  }
  public static void setListeProxy(List<Proxy> listeProxy) {
    ServiceGestionGenerator.listeProxy = listeProxy;
  }
  public static List<Criteria> getListeCriteria() {
    return listeCriteria;
  }
  public static void setListeCriteria(List<Criteria> listeCriteria) {
    ServiceGestionGenerator.listeCriteria = listeCriteria;
  }

  public static boolean isMultiOccurence() {
    return multiOccurence;
  }

  public static void setMultiOccurence(boolean multiOccurence) {
    ServiceGestionGenerator.multiOccurence = multiOccurence;
  }
  public ServiceGestionGenerator(String pathInput,String pathOutput, String packageName,String name,String suffixe){
    this.packageName=packageName;
    this.pathOutput=pathOutput;
    this.pathInput=pathInput;
    this.name=name;
    this.suffixe=suffixe;
  }
  
  public static void main(String[] argv) throws Exception {
    generateServiceForGestion();
  }
  
  public static void generateServiceForGestion() throws Exception {
    //la liste de criteria est obligatoire, le service parameters le bean,le proxy seront créer à partir de cette liste
    setMultiOccurence(true);  
    List<Criteria> listeDataCriteria = gettingCriteriaFromFile(pathInput);
    setListeCriteria(listeDataCriteria);
    generateProxyClass(pathOutput, name, listeDataCriteria);
    //extractionCompute(listeProxy);
    generateServiceClass(pathOutput, listeDataCriteria);
    generateCriteriaBeanClass(pathOutput, listeDataCriteria);
    
    /*if (containsSomme(listeDataProxy)){
      SumServiceGenerator sumServiceGenerator= new SumServiceGenerator(pathInput,pathOutput, packageName, name, suffixe,listeDataProxy);
      SumServiceGenerator.generateSumService();
    }
    if (isExport()){
      String code=null;
      TypeExport exp=extractExport();
      if (exp!=null){
        code=exp.getCodeTable();
      }
      ExportServiceGenerator exportServiceGenerator=new ExportServiceGenerator(pathInput,pathOutput, packageName, name, suffixe,listeDataProxy,exp);
      exportServiceGenerator.generateExportService();
    }
    */
  }
  
  
  
	private static void generateServiceClass(String path, 
										 	 List<Criteria> dataList) {
		try {
			File file = new File(path+name+"Service.java");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
			out.append(declarePackage());
			out.append(declareServiceImportServiceClass(dataList));
			out.append(declareServiceClassCommentary());
			out.append(declareServiceClassDeclaration());
			out.append(declareServiceClassSQLClause());
			//out.append(declareServiceClassSQLClauseCriteria(dataList));

			out.append(declareServiceNameVersionDescription());
			out.append(declareServiceClassConstructor());
			out.append(declareServiceCriteriaCommentary());
			out.append(declareServiceCriteriaClass(dataList));
			

      out.append(declareServiceParametersCommentary());
      out.append(declareServiceParametersClass(dataList));
			
			out.append(declareServiceProcessFindByPrimaryKey(dataList));	
			out.append(declareServiceProcessCountForPrimaryKey());
			
			out.append(declareProcessInitialize());
			out.append(declareProcessCreate());
			out.append(declareProcessUpdate());
			out.append(declareProcessDelete());
			
			/*
			out.append(declare_computeSQLWhereClauseFromServiceCriteriaForPanier(dataList));
			out.append(declareComputes(listeProxy));
			
			if (isExport()){
			  out.append(declareMethodeForExportBureautique());
			}*/
			
			out.append("}//fin classe ").append(name).append("Service");
			out.close();
		} catch (Exception e) {
		  e.printStackTrace();
			System.out.println("erreur durant la génération du service");
		}	
	}

	
	
	private static String declareServiceProcessFindByCriteria(
	                                                            List<Criteria> dataList,List<Proxy> listeProxy) {
	  StringBuilder sb = new StringBuilder();
	  sb.append(" /**\n");
	  sb.append(" * Permet de rechercher la liste des elements\n");
	  sb.append(" * @param cnx connexion de lecture au sein de laquelle sera entreprise la \n");
	  sb.append(" * recherche\n");
	  sb.append(" * @param context contexte permettant d'accéder au service et de prendre en\n");
	  sb.append(" * compte par exemple les droits d'accès\n");
	  sb.append(" * @param criteria ensemble des valeurs qui permettront de sélectionner l'objet\n");
	  sb.append(" * métier (i.e. clé primaire)\n");
	  sb.append(" * @return une réponse contenant un et un seul élément correspondant à l'objet\n");
	  sb.append(" * métier sélectionné si aucune erreur fonctionnelle n'a été rencontrée\n");
	  sb.append(" * @throws ServiceObjectNotFoundException soulevée si l'objet recherché\n");
	  sb.append(" * n'existe pas\n");
	  sb.append(" * @throws ServiceException soulevée si une erreur technique ne permet pas\n");
	  sb.append(" * d'extraire les informations de la couche de persistance\n");
	  sb.append(" */\n");
	  sb.append(" @Override\n");
	  sb.append(" protected ServiceResponse processFindByCriteria(Connection cnx, ServiceContext context,\n");
	  sb.append("                                               ServiceCriteria criteria,\n");
	  sb.append("                                               ServiceFindAttributes findAttributes) throws ServiceException {\n");
	  sb.append(" ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
	  sb.append(" "+name+"ServiceCriteria serviceCriteria = ("+name+"ServiceCriteria) criteria;\n");
	  sb.append(" \n");
	  sb.append(" \n");
	  sb.append(" PreparedStatement stmt = null;\n");
	  sb.append(" ResultSet rs = null;\n");
	  sb.append(" try {\n");
	  sb.append("   SQLCriteria sqlCriteria = new SQLCriteria();\n");
	  sb.append("   StringBuffer sql = new StringBuffer();\n");	  
	  sb.append("   sql.append(_SQL_PRINCIPAL_SELECT);\n");
	  sb.append("   sql.append(_SQL_PRINCIPAL_FROM);\n");
	  sb.append("   sql.append(_SQL_PRINCIPAL_WHERE);\n");
	  sb.append("   \n");
	  if (!traitementSynthese){
	    sb.append("   sqlCriteria = _computeSQLWhereClauseFromServiceCriteria(serviceCriteria, sqlCriteria);\n");
	    sb.append("   if (sqlCriteria.getSQL() != null) {\n");
	    sb.append("     sql.append(sqlCriteria.getSQL());\n");
	    sb.append("   }\n");
	  }
	  sb.append("   \n");
	  sb.append("   sql.append(new SQLServiceFilterTools().computeFiltrerByClause(criteria,_champsFiltres));\n");
	  sb.append("   String orderClause = computeSQLOrderClause(findAttributes, _ORDER_PROPERTIES, _ORDER_COLUMN_PROPERTIES, _ORDER_BY_DEFAULT);\n");
	  sb.append("   if(null != orderClause) {\n");
	  sb.append("     sql.append(\" ORDER BY \");\n");
	  sb.append("     sql.append(orderClause);\n");
	  sb.append("   }\n");
	  sb.append("   \n");
	  sb.append("   stmt = cnx.prepareStatement(sql.toString());\n");
	  
	  if (!traitementSynthese){
	    sb.append("   if (sqlCriteria.getSQL() != null) {\n");
	    sb.append("     sqlCriteria.assignCriteria(stmt);\n");
	    sb.append("   }\n");
	  }else{
	    sb.append(traitementParametreEntree());
	  }
	  sb.append("   rs = stmt.executeQuery();\n");
	  sb.append("   rs = adaptResultSetForServiceFindAttributes(findAttributes, rs);\n");
	  sb.append("  \n");
	  sb.append("   // Liste de proxy à renvoyer\n");
	  sb.append("  "+name+"Proxy proxyTmp = null;\n");
	  sb.append("  List<"+name+"Proxy> proxys = new ArrayList<"+name+"Proxy>();\n");
	  sb.append("  while (rs.next()) {\n");
	  sb.append("     // On construit le proxy\n");
	  sb.append("     proxyTmp = new "+name+"Proxy();\n");
	  sb.append("      //remplir le champ GLFAM_COD_famMGCUT_LIB\n");
	  sb.append("     proxyTmp = computeBusinessObjectProxyFromResultSet(rs, proxyTmp);\n");
	  if (listeCompute!=null && listeCompute.size()>0){
      for (TypeCompute typeRequete: listeCompute){
        sb.append("     compute"+typeRequete.getCode()+"(cnx, context, proxyTmp); \n");
      }
    }
    sb.append("     //computeXXX_COD(cnx, context, proxyTmp,CLE); //Falcultatif \n");
	  for (Proxy proxy: listeProxy){
	    if (proxy.isLien()){
	      sb.append("     proxyTmp.set"+proxy.getNom()+"_URL(DonneesMetierLienUtils.getInstance().computeXXX);\n");
	    }
	  }
	  
	  
	  sb.append("     // On ajoute le proxy à la liste\n");
	  sb.append("     proxys.add(proxyTmp);\n");
	  sb.append("   }\n");
	  sb.append("   boolean hasMoreElements = !isResultSetExhausted(findAttributes, rs);\n");
	  sb.append("   serviceResponse = addElements(serviceResponse, proxys, hasMoreElements);\n");
	  sb.append(" } catch (SQLException SQLEx) {\n");
	  sb.append("   // Exception soulevée si une erreur d'entrée/sortie se produit lors de\n");
	  sb.append("   // l'accès à la base de données\n");
	  sb.append("   ServiceException IOEx = new ServiceException(\"Fatal error while accessing to persistence layer\",\n");
	  sb.append("                                                SQLEx);\n");
	  sb.append("   if (isLoggingError())\n");
	  sb.append("     logError(IOEx.getMessage(), SQLEx);\n");
	  sb.append("   throw IOEx;\n");
	  sb.append(" } finally {\n");
	  sb.append("   // On ferme le jeu de données\n");
	  sb.append("   if (rs != null) {\n");
	  sb.append("     try {\n");
	  sb.append("       rs.close();\n");
	  sb.append("     } catch (SQLException ignoreEx) {}\n");
	  sb.append("     rs = null;\n");
	  sb.append("   }\n");
	  sb.append("   // On ferme l'ordre SQL\n");
	  sb.append("   if (stmt != null) {\n");
	  sb.append("     try {\n");
	  sb.append("       stmt.close();\n");
	  sb.append("     } catch (SQLException ignoreEx) {}\n");
	  sb.append("     stmt = null;\n");
	  sb.append("   }\n");
	  sb.append(" }\n");
	  sb.append(" \n");
	  sb.append(" return serviceResponse;\n");
	  sb.append(" }\n");
	  return sb.toString();
	}
	
	private static String declareServiceProcessFindByPrimaryKey(
                                                            List<Criteria> dataList) {
  StringBuilder sb = new StringBuilder();
  sb.append(" /**\n");
  sb.append("  * Recherche un objet métier à partir de sa clé primaire au sein d'un\n");
  sb.append(" * environnement SQL. Cette méthode est à surcharger par tout service\n");
  sb.append(" * désirant mettre à disposition une telle opération.\n");
  sb.append(" * @param cnx connexion au sein de laquelle la recherche devra être entreprise\n");
  sb.append(" * @param context contexte permettant d'accéder au service et de prendre en\n");
  sb.append(" * compte par exemple les droits d'accès\n");
  sb.append(" * @param criteria ensemble des valeurs qui permettront de sélectionner l'objet\n");
  sb.append(" * métier (i.e. clé primaire)\n");
  sb.append(" * @return une réponse contenant un et un seul élément correspondant à l'objet\n");
  sb.append(" * métier sélectionné si aucune erreur fonctionnelle n'a été rencontrée\n");
  sb.append(" * @throws ServiceObjectNotFoundException soulevée si l'objet recherché\n");
  sb.append(" * n'existe pas\n");
  sb.append(" * @throws ServiceException soulevée à chaque appel pour indiquer que la méthode\n");
  sb.append(" * n'est pas disponible au sein de ce service\n");
  sb.append(" *\n");
  sb.append(" * @see #processFindByPrimaryKey(ServiceContext, ServiceCriteria)\n");
  sb.append(" */\n");
  sb.append(" @Override\n");
  sb.append(" protected ServiceResponse processFindByPrimaryKey(Connection cnx,\n");
  sb.append(" ServiceContext context,\n");
  sb.append(" ServiceCriteria criteria)\n");
  sb.append(" throws ServiceException, ServiceObjectNotFoundException {\n");
  sb.append(" \n");    
  sb.append(" ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
  sb.append(" \n");    
  sb.append("     //Déclaration des criteria\n");
  sb.append(" "+name+"ServiceCriteria serviceCriteria =\n"); 
  sb.append(" ("+name+"ServiceCriteria) criteria;\n");
  sb.append(" \n");    
  sb.append(" if (!serviceResponse.hasErrors()) {\n");
  sb.append(" \n");      
  sb.append("       //Déclaration du proxy à renvoyer\n");
  sb.append(" "+name+"Proxy proxy = null;\n");
  sb.append(" \n");      
  sb.append(" PreparedStatement stmt = null;\n");
  sb.append(" ResultSet rs = null;\n");   
  sb.append(" \n");      
  sb.append("   StringBuffer sql = new StringBuffer();\n");   
  sb.append("   sql.append(_SQL_PRINCIPAL_SELECT);\n");
  sb.append("   sql.append(_SQL_PRINCIPAL_FROM);\n");
  sb.append("   sql.append(_SQL_PRINCIPAL_WHERE);\n");
  sb.append(" \n");      
  sb.append(" try { \n");
  sb.append(" stmt = cnx.prepareStatement(sql.toString());\n");
  sb.append(" \n");    
  //traitement des paramètre d'entrée
  sb.append(traitementParametreEntree());
  
  sb.append("         //\n");
  sb.append(" rs = stmt.executeQuery();\n");
  sb.append(" \n");        
  sb.append(" proxy = new "+name+"Proxy();\n");   
  sb.append(" if (rs.next()){\n"); 
  sb.append(" proxy = computeBusinessObjectProxyFromResultSet(rs, proxy);\n");
  sb.append(" \n");        
  if (listeCompute!=null && listeCompute.size()>0){
    for (TypeCompute typeRequete: listeCompute){
      sb.append("     compute"+typeRequete.getCode()+"(cnx, context, proxy); \n");
    }
  }
  
  
  
  sb.append(" }\n");
  sb.append(" addElement(serviceResponse, proxy);\n");
  sb.append(" \n");        
  sb.append(" } catch (SQLException databaseEx) {\n");
  sb.append("         // Exception soulevée par l'accès à la couche de persistance\n");
  sb.append(" ServiceException ioEx = new ServiceException(\"Fatal error while accessing to persistence layer\",\n");
  sb.append(" databaseEx);\n");
  sb.append(" if (isLoggingError()) {\n");
  sb.append(" logError(databaseEx);\n");
  sb.append(" logError(ioEx);\n");
  sb.append(" }\n");
  sb.append(" throw ioEx;\n");
  sb.append(" \n");        
  sb.append(" } finally {\n");
  sb.append("   // On ferme le jeu de données\n");
  sb.append("   if (rs != null) {\n");
  sb.append("     try {\n");
  sb.append("       rs.close();\n");
  sb.append("     } catch (SQLException ignoreEx) {}\n");
  sb.append("     rs = null;\n");
  sb.append("   }\n");
  sb.append("   // On ferme l'ordre SQL\n");
  sb.append("   if (stmt != null) {\n");
  sb.append("     try {\n");
  sb.append("       stmt.close();\n");
  sb.append("     } catch (SQLException ignoreEx) {}\n");
  sb.append("     stmt = null;\n");
  sb.append("   }\n");
  sb.append(" }\n");
  sb.append("       \n");      
  sb.append("     }// if (!serviceResponse.hasErrors()) {\n");
  sb.append("       \n");    
  sb.append("     return serviceResponse;\n");
  sb.append("   }\n");
  return sb.toString();
}

	private static String declareServiceProcessCount() {
	  StringBuilder sb = new StringBuilder();

	  sb.append("/** \n");
	  sb.append(" * @see sopra.immobilier.service.business.AbstractSQLService#processCount(java.sql.Connection, sopra.immobilier.service.business.ServiceContext, sopra.immobilier.service.business.ServiceCriteria)\n");
	  sb.append(" */\n");
	  sb.append("@Override\n");
	  sb.append("protected ServiceResponse processCount(Connection cnx,\n");
	  sb.append("                                       ServiceContext context,\n");
	  sb.append("                                       ServiceCriteria criteria) throws ServiceException {\n");
	  sb.append("  ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
	  sb.append("  "+name+"ServiceCriteria serviceCriteria = ("+name+"ServiceCriteria) criteria;\n");
	  sb.append(" \n");
	  sb.append("  \n");
	  sb.append("  PreparedStatement stmt = null;\n");
	  sb.append("  ResultSet rs = null;\n");
	  sb.append("  try {\n");
	  sb.append("    SQLCriteria sqlCriteria = new SQLCriteria();\n");
	  sb.append("    StringBuffer sql = new StringBuffer();\n");
	  sb.append("    sql.append(_SQL_PRINCIPAL_COUNT);\n");
	  sb.append("   sql.append(_SQL_PRINCIPAL_FROM);\n");
	  sb.append("   sql.append(_SQL_PRINCIPAL_WHERE);\n");
	  sb.append("    \n");
	  if (!traitementSynthese){
	  sb.append("    sqlCriteria = _computeSQLWhereClauseFromServiceCriteria(serviceCriteria, sqlCriteria,context);\n");
	  sb.append("    if (sqlCriteria.getSQL() != null) {\n");
	  sb.append("      sql.append(sqlCriteria.getSQL());\n");
	  sb.append("    }\n");
	  }
	  sb.append("    \n");
	  sb.append("    stmt = cnx.prepareStatement(sql.toString());\n");
	  if (!traitementSynthese){
	  sb.append("    if (sqlCriteria.getSQL() != null) {\n");
	  sb.append("      sqlCriteria.assignCriteria(stmt);\n");
	  sb.append("    }\n");
	  }else{
	    sb.append(traitementParametreEntree());
	  }
	  sb.append("    rs = stmt.executeQuery();\n");
	  sb.append("    \n");
	  sb.append("    Integer count = new Integer(0);\n");
	  sb.append("    if (rs.next()) {\n");
	  sb.append("      count = new Integer(rs.getInt(1));\n");
	  sb.append("    }\n");
	  sb.append("    serviceResponse = addElement(serviceResponse, count);\n");
	  sb.append("  } catch (SQLException SQLEx) {\n");
	  sb.append("   // Exception soulevée si une erreur d'entrée/sortie se produit lors de\n");
	  sb.append("    // l'accès à la base de données\n");
	  sb.append("    ServiceException IOEx = new ServiceException(\"Fatal error while accessing to persistence layer\",\n");
	  sb.append("                                                 SQLEx);\n");
	  sb.append("    if (isLoggingError())\n");
	  sb.append("      logError(IOEx.getMessage(), SQLEx);\n");
	  sb.append("   throw IOEx;\n");
	  sb.append(" } finally {\n");
	  sb.append("   // On ferme le jeu de données\n");
	  sb.append("   if (rs != null) {\n");
	  sb.append("      try {\n");
	  sb.append("        rs.close();\n");
	  sb.append("      } catch (SQLException ignoreEx) {}\n");
	  sb.append("      rs = null;\n");
	  sb.append("    }\n");
	  sb.append("    // On ferme l'ordre SQL\n");
	  sb.append("    if (stmt != null) {\n");
	  sb.append("      try {\n");
	  sb.append("        stmt.close();\n");
	  sb.append("      } catch (SQLException ignoreEx) {}\n");
	  sb.append("      stmt = null;\n");
	  sb.append("    }\n");
	  sb.append("  }\n");
	  sb.append("  \n");
	  sb.append("  return serviceResponse;\n");
	  sb.append("}\n");
    
    return sb.toString();
	}
	
	private static String traitementParametreEntree(){
	  StringBuilder sb = new StringBuilder();
	  int i=1;
	  for(Criteria criteria:listeCriteria){
	    if (criteria.isObligatoire()){
	      if (criteria.getType().equals("Integer")){
	        sb.append(" stmt.setInt("+i+", serviceCriteria.get"+criteria.getNom()+"().intValue()); \n");
	      }else if (criteria.getType().equals("Date")){
	        sb.append(" stmt.set"+criteria.getType()+"("+i+", DateTools.getInstance().asSQLDate(serviceCriteria.get"+criteria.getNom()+"())); \n");
	      }else{
	        sb.append(" stmt.set"+criteria.getType()+"("+i+", serviceCriteria.get"+criteria.getNom()+"()); \n");
	        
	      }
	      i++;
	    }
	  }
	  return sb.toString();
	}
	
	private static String declareServiceProcessCountForPrimaryKey() {
	  StringBuilder sb = new StringBuilder();
	  
	  sb.append("/** \n");
	  sb.append(" * @see sopra.immobilier.service.business.AbstractSQLService#processCount(java.sql.Connection, sopra.immobilier.service.business.ServiceContext, sopra.immobilier.service.business.ServiceCriteria)\n");
	  sb.append(" */\n");
	  sb.append("@Override\n");
	  sb.append("protected ServiceResponse processCount(Connection cnx,\n");
	  sb.append("                                       ServiceContext context,\n");
	  sb.append("                                       ServiceCriteria criteria) throws ServiceException {\n");
	  sb.append("  ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
	  sb.append("  "+name+"ServiceCriteria serviceCriteria = ("+name+"ServiceCriteria) criteria;\n");
	  sb.append(" \n");
	  sb.append("  \n");
	  sb.append("  PreparedStatement stmt = null;\n");
	  sb.append("  ResultSet rs = null;\n");
	  sb.append("  try {\n");
	  sb.append("    SQLCriteria sqlCriteria = new SQLCriteria();\n");
	  sb.append("    StringBuffer sql = new StringBuffer();\n");
	  sb.append("    sql.append(_SQL_PRINCIPAL_COUNT);\n");
	  sb.append("   sql.append(_SQL_PRINCIPAL_FROM);\n");
	  sb.append("   sql.append(_SQL_PRINCIPAL_WHERE);\n");
	  sb.append("    \n");
	  sb.append("    stmt = cnx.prepareStatement(sql.toString());\n");
	  int i=1;
	  for(Criteria criteria:listeCriteria){
	    if (criteria.getType().equals("Integer")){
	      sb.append(" stmt.setInt("+i+", serviceCriteria.get"+criteria.getNom()+"().intValue()); \n");
	    }else if (criteria.getType().equals("Date")){
	      sb.append(" stmt.set"+criteria.getType()+"("+i+", DateTools.getInstance().asSQLDate(serviceCriteria.get"+criteria.getNom()+"())); \n");
      }else{
        sb.append(" stmt.set"+criteria.getType()+"("+i+", serviceCriteria.get"+criteria.getNom()+"()); \n");
	      
	    }
	    i++;
	  }
	  sb.append("    rs = stmt.executeQuery();\n");
	  sb.append("    \n");
	  sb.append("    Integer count = new Integer(0);\n");
	  sb.append("    if (rs.next()) {\n");
	  sb.append("      count = new Integer(rs.getInt(1));\n");
	  sb.append("    }\n");
	  sb.append("    serviceResponse = addElement(serviceResponse, count);\n");
	  sb.append("  } catch (SQLException SQLEx) {\n");
	  sb.append("   // Exception soulevée si une erreur d'entrée/sortie se produit lors de\n");
	  sb.append("    // l'accès à la base de données\n");
	  sb.append("    ServiceException IOEx = new ServiceException(\"Fatal error while accessing to persistence layer\",\n");
	  sb.append("                                                 SQLEx);\n");
	  sb.append("    if (isLoggingError())\n");
	  sb.append("      logError(IOEx.getMessage(), SQLEx);\n");
	  sb.append("   throw IOEx;\n");
	  sb.append(" } finally {\n");
	  sb.append("   // On ferme le jeu de données\n");
	  sb.append("   if (rs != null) {\n");
	  sb.append("      try {\n");
	  sb.append("        rs.close();\n");
	  sb.append("      } catch (SQLException ignoreEx) {}\n");
	  sb.append("      rs = null;\n");
	  sb.append("    }\n");
	  sb.append("    // On ferme l'ordre SQL\n");
	  sb.append("    if (stmt != null) {\n");
	  sb.append("      try {\n");
	  sb.append("        stmt.close();\n");
	  sb.append("      } catch (SQLException ignoreEx) {}\n");
	  sb.append("      stmt = null;\n");
	  sb.append("    }\n");
	  sb.append("  }\n");
	  sb.append("  \n");
	  sb.append("  return serviceResponse;\n");
	  sb.append("}\n");
	  
	  return sb.toString();
	}
	
	
	private static String declare_computeSQLWhereClauseFromServiceCriteriaForPanier(List<Criteria> dataList){
	  StringBuilder sb = new StringBuilder();
    sb.append("    /**\n");
    sb.append("/**\n");
    sb.append(" * Méthode pour créer la requête de la recherche.\n");
    sb.append(" * \n");
    sb.append(" * @param findCriteria Critères du recherche\n");
    sb.append(" * @param sqlCriteria SQLCriteria\n");
    sb.append(" * \n");
    sb.append(" * @return PreparedStatement\n");
    sb.append(" * @throws SQLException SQLException\n");
    sb.append(" * @throws ServiceException ServiceException\n");
    sb.append(" */\n");
    sb.append("private SQLCriteria _computeSQLWhereClauseFromServiceCriteria("+name+"ServiceCriteria findCriteria,\n");
    sb.append("                                                              SQLCriteria sqlCriteria,ServiceContext context\n");
    sb.append("                                                              ) throws ServiceException, SQLException {\n");
    sb.append("   \n");
    sb.append("    // Ajout de critères du recherche\n");
    
    for (Criteria critere : dataList) {
      if (critere!=null){
        if (critere.getNom().contains("_LIB") && critere.getType().equalsIgnoreCase("String")){
          sb.append("    if(findCriteria.get"+critere.getNom()+"() != null && !findCriteria.get"+critere.getNom()+"().equals(\"\")){\n");
          sb.append("      sqlCriteria.appendCriterion(_SQL_"+critere.getNom().toUpperCase()+",findCriteria.get"+critere.getNom()+"().replace('*', '%') );\n");
          sb.append("    }\n");
        }else if (critere.getType().equalsIgnoreCase("String")){
            sb.append("    if(findCriteria.get"+critere.getNom()+"() != null && !findCriteria.get"+critere.getNom()+"().equals(\"\")){\n");
            sb.append("      sqlCriteria.appendCriterion(_SQL_"+critere.getNom().toUpperCase()+",findCriteria.get"+critere.getNom()+"());\n");
            sb.append("    }\n");
        }else if (critere.getType().equalsIgnoreCase("Boolean")){
          sb.append("    if(findCriteria.get"+critere.getNom().toUpperCase()+"() != null && findCriteria.get"+critere.getNom()+"().booleanValue()){\n");
          sb.append("      sqlCriteria.appendStaticCriterion(_SQL_"+critere.getNom().toUpperCase()+");\n");
          sb.append("    }\n");
        }else if (critere.getType().equalsIgnoreCase("Date")){
          sb.append("    if(findCriteria.get"+critere.getNom().toUpperCase()+"() != null ){\n");
          sb.append("      sqlCriteria.appendCriterion(_SQL_"+critere.getNom().toUpperCase()+",DateTools.getInstance().asSQLDate(findCriteria.get"+critere.getNom()+"()));\n");
          sb.append("    }\n");
        }else{
        sb.append("    if(findCriteria.get"+critere.getNom()+"() != null){\n");
        sb.append("      sqlCriteria.appendCriterion(_SQL_"+critere.getNom().toUpperCase()+",findCriteria.get"+critere.getNom()+"());\n");
        sb.append("    }\n");
        }
      }
    }
    for (String composant : listeComposantMetier) {
      if (composant.equalsIgnoreCase("paesi")){
        sb.append("sqlCriteria.appendStaticCriterion(_SQL_"+composant.toUpperCase()+" _computeESICriteriaWhereClause(context, findCriteria.getPAESI_CODEXT(), findCriteria.getINDPAESIInferieur()), new String[]{\"TABLE REQUETE\"});\n");
      }
    }
    sb.append("    \n");
    sb.append("  \n");
    sb.append("  return sqlCriteria;\n");
    sb.append("}\n");
    
    return sb.toString();
	}
	      
	private static String declareComputes(List<Proxy> listeProxy){
	  StringBuilder sb = new StringBuilder();
	  if (listeCompute!=null && listeCompute.size()>0){
      for (TypeCompute typeCompute: listeCompute){
        sb.append(declare_Compute(listeProxy, typeCompute));
      }
    }
	  
	  if (listeComposantMetier!=null && listeComposantMetier.size()>0){
	    for (String typeComposantMetier: listeComposantMetier){
	      if (typeComposantMetier!=null && typeComposantMetier.equalsIgnoreCase("TIERS")){
	        sb.append(declare_ComposantMetierTIERS());
	      }else if(typeComposantMetier!=null && typeComposantMetier.equalsIgnoreCase("CONTRAT")){
	        sb.append(declare_ComposantMetierCONTRAT());
	      }else if(typeComposantMetier!=null && typeComposantMetier.equalsIgnoreCase("CONVENTION")){
	        sb.append(declare_ComposantMetierCONVENTION());
	      }else if(typeComposantMetier!=null && typeComposantMetier.equalsIgnoreCase("ESI")){
	        sb.append(declare_ComposantMetierESI());
	      }else if(typeComposantMetier!=null && typeComposantMetier.equalsIgnoreCase("ESO")){
	        sb.append(declare_ComposantMetierESO());
	      }else if(typeComposantMetier!=null && typeComposantMetier.equalsIgnoreCase("INTITULE")){
	        sb.append(declare_ComposantMetierINTITULE());
	      }
	      
	    }
	  }
	  return sb.toString();
	}
	
	private static String declare_Compute(List<Proxy> listeProxy, TypeCompute typeCompute){
	  StringBuilder sb = new StringBuilder();
	  sb.append("/**\n");
	  sb.append(" * Cette méthode permet d'exécuter la requete secondaire permettant de recuperer "+typeCompute.getCode()+".\n");
	  sb.append(" * @param cnx connection\n");
	  sb.append(" * @param context contexte\n");
	  sb.append(" * @param proxy \n");
	  sb.append(" * @throws ServiceObjectNotFoundException Erreur technique\n");
	  sb.append(" * @throws ServiceException Erreur technique\n");
	  sb.append(" */\n");
	  sb.append("protected void compute"+typeCompute.getCode()+"(Connection cnx, ServiceContext context,"+name+"Proxy proxy ) \n");
	  sb.append("throws ServiceObjectNotFoundException, ServiceException{\n");
	  
	  //traitement du type de compute requete
	  
	  if (typeCompute.getType()!=null && typeCompute.getType().equals("REQ") ){
	    sb.append(computeRequete(listeProxy, typeCompute));
	  }else if (typeCompute.getType()!=null && typeCompute.getType().equals("SER")){
	    sb.append(computeService(listeProxy, typeCompute));
	  }else if (typeCompute.getType()!=null && typeCompute.getType().equals("COD")){
	    sb.append(computeCodification(listeProxy, typeCompute));
	  }else if (typeCompute.getType()!=null && typeCompute.getType().equals("COM")){
      sb.append(computeComposantMetier(listeProxy, typeCompute));
    }
	  
	  
	  sb.append("}\n");
    return sb.toString();
  }
	
	private static String computeRequete(List<Proxy> listeProxy, TypeCompute typeCompute){
	  StringBuilder sb = new StringBuilder();
    if( typeCompute.getListeParametre()!=null){
      sb.append("if (");
      int i=0;
      for(String param:typeCompute.getListeParametre()){
        if (i>0){sb.append(" && ");} 
        sb.append("proxy.get"+param+"()!=null ");
        i++;
      }
      sb.append("){\n");
    }
  
    
    sb.append("\n");
    sb.append("PreparedStatement stmt = null;\n");
    sb.append("ResultSet rs = null;\n");
    sb.append("try {\n");
    sb.append("StringBuffer sql = new StringBuffer();\n");
    sb.append("sql.append("+typeCompute.getRequete()+");\n");
    sb.append("\n");
    sb.append("    stmt = cnx.prepareStatement(sql.toString());\n");
    int j=1;
    if (typeCompute.getListeParametre()!=null){
      for(String param:typeCompute.getListeParametre()){
        sb.append("stmt.setXX("+j+", proxy.get"+param+"());\n");
        j++;
      }
    }
    sb.append("\n");
    sb.append("rs = stmt.executeQuery();\n");
    sb.append("\n");
    sb.append("if (rs.next()) {\n");
    
     //traitement du resultat de la requete et affectation des valeur au proxy
    for (Proxy proxy: listeProxy){
      if (proxy.getTypeCompute()!=null && proxy.getTypeCompute().getCode().equals(typeCompute.getCode())){
        if (proxy.getType().equalsIgnoreCase("Integer")){
          sb.append("proxy.set"+proxy.getNom()+"(rs.getInt(\""+proxy.getNom()+"\"));\n");
        }else{
          sb.append("proxy.set"+proxy.getNom()+"(rs.get"+proxy.getType()+"(\""+proxy.getNom()+"\"));\n");
        }
      }
    }
    sb.append("}\n");
    sb.append("\n");
    sb.append("} catch (SQLException SQLEx) {\n");
    sb.append("          // Exception soulevée si une erreur d'entrée/sortie se produit lors de\n");
    sb.append("          // l'accès à la base de données\n");
    sb.append("          ServiceException IOEx = new ServiceException(\"Fatal error while accessing to persistence layer\",\n");
    sb.append("                                                       SQLEx);\n");
    sb.append("          if (isLoggingError())\n");
    sb.append("            logError(IOEx.getMessage(), SQLEx);\n");
    sb.append("          throw IOEx;\n");
    sb.append("        } finally {\n");
    sb.append("       // On ferme le jeu de données\n");
    sb.append("          if (rs != null) {\n");
    sb.append("             try {\n");
    sb.append("               rs.close();\n");
    sb.append("             } catch (SQLException ignoreEx) {}\n");
    sb.append("             rs = null;\n");
    sb.append("           }\n");
    sb.append("           // On ferme l'ordre SQL\n");
    sb.append("           if (stmt != null) {\n");
    sb.append("             try {\n");
    sb.append("               stmt.close();\n");
    sb.append("             } catch (SQLException ignoreEx) {}\n");
    sb.append("             stmt = null;\n");
    sb.append("           }\n");
    sb.append("}\n"); 
    if( typeCompute.getListeParametre()!=null){
      sb.append("}\n"); 
    }
    return sb.toString();
	}
	
	private static String computeService(List<Proxy> listeProxy, TypeCompute typeCompute){
	  StringBuilder sb = new StringBuilder();
	  //traitement du type de compute service
    sb.append("ServiceCriteria criteria = createDefaultServiceCriteria();\n");
    sb.append("ServiceResponse reponse = createDefaultServiceResponse();\n");
    sb.append("criteria = createDefaultServiceCriteria();\n");
    sb.append("//criteria.addCriterion("+typeCompute.getService()+"ServiceCriteria.XX_CRITERION_NAME, proxy.getXXX());\n");
    //traitement des paramètres
    if( typeCompute.getListeParametre()!=null){
    for (String param: typeCompute.getListeParametre()){
      sb.append("criteria.addCriterion("+typeCompute.getService()+"ServiceCriteria."+param+"_CRITERION_NAME, proxy.get"+param+"());\n");
    }
    }
    sb.append("try {\n");
    sb.append("reponse= resolveServiceResponseByPrimaryKey("+typeCompute.getService()+"Service.SERVICE_NAME, context, criteria);\n");
    sb.append(" }\n");
    sb.append("catch (ServiceObjectNotFoundException e) {\n");
    sb.append("  if (isLoggingError()) {\n");
    sb.append("    logError(e);\n");
    sb.append("  }\n");
    sb.append("}\n");
    sb.append("if(!reponse.hasErrors() && reponse.getElement()!=null){\n");
    sb.append("  // dans notre cas, une seule codif correspond à la valeur interne\n");
    sb.append("  "+typeCompute.getService()+"Proxy result = ("+typeCompute.getService()+"Proxy) reponse.getElement();\n");
    sb.append("  if(result != null){\n");
    
    //traitement du resultat de la requete et affectation des valeur au proxy
    for (Proxy proxy: listeProxy){
      if (proxy.getTypeCompute()!=null && proxy.getTypeCompute().getCode().equals(typeCompute.getCode())){
        sb.append("  proxy.set"+proxy.getNom()+"(proxy.get"+proxy.getNom()+"()));  \n");
      }
    }
    sb.append("}\n");
    sb.append("}\n");
    return sb.toString();
	}
	
	private static String computeCodification(List<Proxy> listeProxy, TypeCompute typeCompute){
    StringBuilder sb = new StringBuilder();
  //traitement du type de compute service
    sb.append(" ServiceCriteria criteria = createDefaultServiceCriteria();\n");
    sb.append("ServiceResponse reponse = createDefaultServiceResponse();\n");
    sb.append(" criteria = createDefaultServiceCriteria();\n");
    if( typeCompute.getListeParametre()!=null && typeCompute.getListeParametre().size()>=1){
      sb.append(" criteria.addCriterion(CodificationValeurServiceCriteria.MGTCO_COD_CRITERION_NAME, \""+typeCompute.getListeParametre().get(0)+"\");\n");
    }
    if( typeCompute.getListeParametre()!=null && typeCompute.getListeParametre().size()>=2){
      sb.append(" criteria.addCriterion(CodificationValeurServiceCriteria.COD_CRITERION_NAME, proxy.get"+typeCompute.getListeParametre().get(1)+"());\n");
    }
    if( typeCompute.getListeParametre()!=null){
      for (String param: typeCompute.getListeParametre()){
        sb.append("criteria.addCriterion("+typeCompute.getService()+"ServiceCriteria."+param+"_CRITERION_NAME, proxy.get"+param+"());\n");
      }
    }
    sb.append("try {\n");
    sb.append("  reponse= resolveServiceResponseByPrimaryKey(CodificationValeurService.SERVICE_NAME, context, criteria);\n");
    sb.append(" }\n");
    sb.append("catch (ServiceObjectNotFoundException e) {\n");
    sb.append("  if (isLoggingError()) {\n");
    sb.append("    logError(e);\n");
    sb.append("  }\n");
    sb.append("}\n");
    
    sb.append("  if(!reponse.hasErrors() && reponse.getElement()!=null){\n");
    sb.append("    // dans notre cas, une seule codif correspond à la valeur interne\n");
    sb.append("   CodificationValeurProxy result = (CodificationValeurProxy) reponse.getElement();\n");
    sb.append("   if(result != null){\n");
    sb.append("      proxy.set"+typeCompute.getCode()+"(result.getCOD()+\" \"+result.getLIB());  \n");
    sb.append("  }\n");
    sb.append("  }\n");
  
    return sb.toString();
  }
	
	private static String computeComposantMetier(List<Proxy> listeProxy, TypeCompute typeCompute){
    StringBuilder sb = new StringBuilder();
  //traitement du type de compute service
    sb.append("//Récupère l'information du composant métier\n");
    if (typeCompute.getTypeComposantMetier()!=null && typeCompute.getTypeComposantMetier().equalsIgnoreCase("TIERS")){
      sb.append("  ComposantsMetiersAffichageTiersProxy proxyTMP = _ComposantMetierTIERS(cnx, context");
    }else if(typeCompute.getTypeComposantMetier()!=null && typeCompute.getTypeComposantMetier().equalsIgnoreCase("CONTRAT")){
      sb.append("  ComposantsMetiersAffichageContratProxy proxyTMP = _ComposantMetierCONTRAT(cnx, context");
    }else if(typeCompute.getTypeComposantMetier()!=null && typeCompute.getTypeComposantMetier().equalsIgnoreCase("CONVENTION")){
      sb.append("  ComposantsMetiersAffichageConventionProxy proxyTMP = _ComposantMetierCONVENTION(cnx, context");
    }else if(typeCompute.getTypeComposantMetier()!=null && typeCompute.getTypeComposantMetier().equalsIgnoreCase("ESI")){
      sb.append("  ComposantsMetiersAffichageESIProxy proxyTMP = _ComposantMetierESI(cnx, context");
    }else if(typeCompute.getTypeComposantMetier()!=null && typeCompute.getTypeComposantMetier().equalsIgnoreCase("ESO")){
      sb.append("  ComposantsMetiersAffichageESOProxy proxyTMP = _ComposantMetierESO(cnx, context");
    }else if(typeCompute.getTypeComposantMetier()!=null && typeCompute.getTypeComposantMetier().equalsIgnoreCase("INTITULE")){
      sb.append("  ComposantsMetiersAffichageIntituleProxy proxyTMP = _ComposantMetierINTITULE(cnx, context");
    }
    //ajout des parametres
    for (String param: typeCompute.getListeParametre()){
      sb.append(", proxy.get"+param+"()");
    }
    sb.append(");\n");
    
    
    sb.append(" if(proxyTMP != null){\n");
    for (Proxy proxy: listeProxy){
      if (proxy.getTypeCompute()!=null && proxy.getTypeCompute().getCode().equals(typeCompute.getCode())){
        sb.append("  proxy.set"+proxy.getNom()+"(proxyTMP.get"+proxy.getNom()+"());  \n");
      }
    }
    sb.append("}\n");
  
    return sb.toString();
  }
	
	private static String declare_ComposantMetierTIERS(){
	  StringBuilder sb = new StringBuilder();
	  sb.append(" /**\n");
	  sb.append(" * @param cnx Connection\n");
	  sb.append("  * @param serviceContext contexte utilisateur\n");
	  sb.append("  * @param COD le code\n");
	  sb.append("  * @return l'information\n");
	  sb.append("  * @throws ServiceException exception\n");
	  sb.append("  */\n");
	  sb.append(" private ComposantsMetiersAffichageTiersProxy _ComposantMetierTIERS(Connection cnx,\n");
	  sb.append("                                                                             ServiceContext serviceContext,\n");
	  sb.append("                                                                              Integer COD)\n");
	  sb.append(" throws ServiceException {\n");
	  sb.append(" \n");
	  sb.append("ComposantsMetiersAffichageTiersProxy proxy = null;\n");
	  sb.append("\n");
	  sb.append(" if(COD != null){\n");
	  sb.append("   \n");
	  sb.append("   ServiceCriteria criteria = createDefaultServiceCriteria();  \n");
	  sb.append("   criteria.addCriterion(ComposantsMetiersAffichageTiersPrimaryKeyServiceCriteria.TYPE_COMPOSANT_CRITERION_NAME,\n"); 
	  sb.append("                         ULISConstantes.COMPOSANT_METIER_TIERS);\n");
	  sb.append("   criteria.addCriterion(ComposantsMetiersAffichageTiersPrimaryKeyServiceCriteria.TOTIE_COD_CRITERION_NAME, COD);\n");
	  sb.append("    \n");
	  sb.append("    try {\n");
	  sb.append("      \n");
	  sb.append("      ServiceResponse serviceResponse = \n");
	  sb.append("       resolveServiceResponseByPrimaryKey(cnx, ComposantsMetiersAffichageService.SERVICE_NAME, serviceContext, criteria);\n");
	  sb.append("        \n");
	  sb.append("      if (! serviceResponse.hasErrors()) {        \n");
	  sb.append("        proxy = (ComposantsMetiersAffichageTiersProxy) serviceResponse.getElement();\n");
	  sb.append("      } else {\n");
	  sb.append("        //erreur lors de l'appel du service imbriqué, on lance une ServiceException\n");
	  sb.append("        ServiceException servEx = new ServiceException(\"L'appel du service métier \" + ComposantsMetiersAffichageService.SERVICE_NAME\n");
	  sb.append("                                                       + \" avec les critères \" + criteria.toString()\n");
	  sb.append("                                                       + \" a généré des erreurs \" + serviceResponse.getErrors() );\n");
	  sb.append("        if (isLoggingError()) {\n");
	  sb.append("           logError(servEx);\n");
	  sb.append("        }\n");
	  sb.append("        throw servEx;\n");
	  sb.append("       }\n");
	  sb.append("      \n");
	  sb.append("    } catch (ServiceObjectNotFoundException ex){\n");
	  sb.append("      proxy = null;\n");
	  sb.append("    }//try {\n");
	  sb.append("    \n");
	  sb.append("  }//if(COD != null){\n");
	  sb.append("  \n");
	  sb.append("  return proxy;\n");
	  sb.append(" }\n");
	  return sb.toString();
	}
	private static String declare_ComposantMetierCONTRAT(){
    StringBuilder sb = new StringBuilder();
    sb.append(" /**\n");
    sb.append(" * @param cnx Connection\n");
    sb.append("  * @param serviceContext contexte utilisateur\n");
    sb.append("  * @param COD le code\n");
    sb.append("  * @return l'information\n");
    sb.append("  * @throws ServiceException exception\n");
    sb.append("  */\n");
    sb.append(" private ComposantsMetiersAffichageContratProxy _ComposantMetierCONTRAT(Connection cnx,\n");
    sb.append("                                                                             ServiceContext serviceContext,\n");
    sb.append("                                                                              Integer NUM,Integer NUMVER)\n");
    sb.append(" throws ServiceException {\n");
    sb.append(" \n");
    sb.append("ComposantsMetiersAffichageContratProxy proxy = null;\n");
    sb.append("\n");
    sb.append(" if(NUM != null){\n");
    sb.append("   \n");
    sb.append("   ServiceCriteria criteria = createDefaultServiceCriteria();  \n");
    sb.append("   criteria.addCriterion(ComposantsMetiersAffichageContratPrimaryKeyServiceCriteria.TYPE_COMPOSANT_CRITERION_NAME,\n"); 
    sb.append("                         ULISConstantes.COMPOSANT_METIER_CONTRAT);\n");
    sb.append("   criteria.addCriterion(ComposantsMetiersAffichageContratPrimaryKeyServiceCriteria.GLCON_NUM_CRITERION_NAME, NUM);\n");
    sb.append("   criteria.addCriterion(ComposantsMetiersAffichageContratPrimaryKeyServiceCriteria.GLCON_NUMVER_CRITERION_NAME, NUMVER);\n");
    sb.append("    \n");
    sb.append("    try {\n");
    sb.append("      \n");
    sb.append("      ServiceResponse serviceResponse = \n");
    sb.append("       resolveServiceResponseByPrimaryKey(cnx, ComposantsMetiersAffichageService.SERVICE_NAME, serviceContext, criteria);\n");
    sb.append("        \n");
    sb.append("      if (! serviceResponse.hasErrors()) {        \n");
    sb.append("        proxy = (ComposantsMetiersAffichageContratProxy) serviceResponse.getElement();\n");
    sb.append("      } else {\n");
    sb.append("        //erreur lors de l'appel du service imbriqué, on lance une ServiceException\n");
    sb.append("        ServiceException servEx = new ServiceException(\"L'appel du service métier \" + ComposantsMetiersAffichageService.SERVICE_NAME\n");
    sb.append("                                                       + \" avec les critères \" + criteria.toString()\n");
    sb.append("                                                       + \" a généré des erreurs \" + serviceResponse.getErrors() );\n");
    sb.append("        if (isLoggingError()) {\n");
    sb.append("           logError(servEx);\n");
    sb.append("        }\n");
    sb.append("        throw servEx;\n");
    sb.append("       }\n");
    sb.append("      \n");
    sb.append("    } catch (ServiceObjectNotFoundException ex){\n");
    sb.append("      proxy = null;\n");
    sb.append("    }//try {\n");
    sb.append("    \n");
    sb.append("  }//if(COD != null){\n");
    sb.append("  \n");
    sb.append("  return proxy;\n");
    sb.append(" }\n");
    return sb.toString();
  }
	 private static String declare_ComposantMetierCONVENTION(){
	    StringBuilder sb = new StringBuilder();
	    sb.append(" /**\n");
	    sb.append(" * @param cnx Connection\n");
	    sb.append("  * @param serviceContext contexte utilisateur\n");
	    sb.append("  * @param COD le code\n");
	    sb.append("  * @return l'information\n");
	    sb.append("  * @throws ServiceException exception\n");
	    sb.append("  */\n");
	    sb.append(" private ComposantsMetiersAffichageConventionProxy _ComposantMetierCONVENTION(Connection cnx,\n");
	    sb.append("                                                                             ServiceContext serviceContext,\n");
	    sb.append("                                                                              Integer NUM,Integer NUMVER)\n");
	    sb.append(" throws ServiceException {\n");
	    sb.append(" \n");
	    sb.append("ComposantsMetiersAffichageConventionProxy proxy = null;\n");
	    sb.append("\n");
	    sb.append(" if(NUM != null){\n");
	    sb.append("   \n");
	    sb.append("   ServiceCriteria criteria = createDefaultServiceCriteria();  \n");
	    sb.append("   criteria.addCriterion(ComposantsMetiersAffichageConventionPrimaryKeyServiceCriteria.TYPE_COMPOSANT_CRITERION_NAME,\n"); 
	    sb.append("                         ULISConstantes.COMPOSANT_METIER_CONVENTION);\n");
	    sb.append("   criteria.addCriterion(ComposantsMetiersAffichageConventionPrimaryKeyServiceCriteria.FICON_NUM_CRITERION_NAME, NUM);\n");
	    sb.append("   criteria.addCriterion(ComposantsMetiersAffichageConventionPrimaryKeyServiceCriteria.FICON_NUMVER_CRITERION_NAME, NUMVER);\n");
	    sb.append("    \n");
	    sb.append("    try {\n");
	    sb.append("      \n");
	    sb.append("      ServiceResponse serviceResponse = \n");
	    sb.append("       resolveServiceResponseByPrimaryKey(cnx, ComposantsMetiersAffichageService.SERVICE_NAME, serviceContext, criteria);\n");
	    sb.append("        \n");
	    sb.append("      if (! serviceResponse.hasErrors()) {        \n");
	    sb.append("        proxy = (ComposantsMetiersAffichageConventionProxy) serviceResponse.getElement();\n");
	    sb.append("      } else {\n");
	    sb.append("        //erreur lors de l'appel du service imbriqué, on lance une ServiceException\n");
	    sb.append("        ServiceException servEx = new ServiceException(\"L'appel du service métier \" + ComposantsMetiersAffichageService.SERVICE_NAME\n");
	    sb.append("                                                       + \" avec les critères \" + criteria.toString()\n");
	    sb.append("                                                       + \" a généré des erreurs \" + serviceResponse.getErrors() );\n");
	    sb.append("        if (isLoggingError()) {\n");
	    sb.append("           logError(servEx);\n");
	    sb.append("        }\n");
	    sb.append("        throw servEx;\n");
	    sb.append("       }\n");
	    sb.append("      \n");
	    sb.append("    } catch (ServiceObjectNotFoundException ex){\n");
	    sb.append("      proxy = null;\n");
	    sb.append("    }//try {\n");
	    sb.append("    \n");
	    sb.append("  }//if(COD != null){\n");
	    sb.append("  \n");
	    sb.append("  return proxy;\n");
	    sb.append(" }\n");
	    return sb.toString();
	  }
	 private static String declare_ComposantMetierINTITULE(){
     StringBuilder sb = new StringBuilder();
     sb.append(" /**\n");
     sb.append(" * @param cnx Connection\n");
     sb.append("  * @param serviceContext contexte utilisateur\n");
     sb.append("  * @param COD le code\n");
     sb.append("  * @return l'information\n");
     sb.append("  * @throws ServiceException exception\n");
     sb.append("  */\n");
     sb.append(" private ComposantsMetiersAffichageIntituleProxy _ComposantMetierINTITULE(Connection cnx,\n");
     sb.append("                                                                             ServiceContext serviceContext,\n");
     sb.append("                                                                              Integer NUM)\n");
     sb.append(" throws ServiceException {\n");
     sb.append(" \n");
     sb.append("ComposantsMetiersAffichageIntituleProxy proxy = null;\n");
     sb.append("\n");
     sb.append(" if(NUM != null){\n");
     sb.append("   \n");
     sb.append("   ServiceCriteria criteria = createDefaultServiceCriteria();  \n");
     sb.append("   criteria.addCriterion(ComposantsMetiersAffichageIntitulePrimaryKeyServiceCriteria.TYPE_COMPOSANT_CRITERION_NAME,\n"); 
     sb.append("                         ULISConstantes.COMPOSANT_METIER_INTITULE);\n");
     sb.append("   criteria.addCriterion(ComposantsMetiersAffichageIntitulePrimaryKeyServiceCriteria.CAINT_NUM_CRITERION_NAME, NUM);\n");
     sb.append("    \n");
     sb.append("    try {\n");
     sb.append("      \n");
     sb.append("      ServiceResponse serviceResponse = \n");
     sb.append("       resolveServiceResponseByPrimaryKey(cnx, ComposantsMetiersAffichageService.SERVICE_NAME, serviceContext, criteria);\n");
     sb.append("        \n");
     sb.append("      if (! serviceResponse.hasErrors()) {        \n");
     sb.append("        proxy = (ComposantsMetiersAffichageIntituleProxy) serviceResponse.getElement();\n");
     sb.append("      } else {\n");
     sb.append("        //erreur lors de l'appel du service imbriqué, on lance une ServiceException\n");
     sb.append("        ServiceException servEx = new ServiceException(\"L'appel du service métier \" + ComposantsMetiersAffichageService.SERVICE_NAME\n");
     sb.append("                                                       + \" avec les critères \" + criteria.toString()\n");
     sb.append("                                                       + \" a généré des erreurs \" + serviceResponse.getErrors() );\n");
     sb.append("        if (isLoggingError()) {\n");
     sb.append("           logError(servEx);\n");
     sb.append("        }\n");
     sb.append("        throw servEx;\n");
     sb.append("       }\n");
     sb.append("      \n");
     sb.append("    } catch (ServiceObjectNotFoundException ex){\n");
     sb.append("      proxy = null;\n");
     sb.append("    }//try {\n");
     sb.append("    \n");
     sb.append("  }//if(COD != null){\n");
     sb.append("  \n");
     sb.append("  return proxy;\n");
     sb.append(" }\n");
     return sb.toString();
   }
   private static String declare_ComposantMetierESI(){
     StringBuilder sb = new StringBuilder();
     sb.append(" /**\n");
     sb.append(" * @param cnx Connection\n");
     sb.append("  * @param serviceContext contexte utilisateur\n");
     sb.append("  * @param COD le code\n");
     sb.append("  * @return l'information\n");
     sb.append("  * @throws ServiceException exception\n");
     sb.append("  */\n");
     sb.append(" private ComposantsMetiersAffichageESIProxy _ComposantMetierESI(Connection cnx,\n");
     sb.append("                                                                             ServiceContext serviceContext,\n");
     sb.append("                                                                              Integer NUM)\n");
     sb.append(" throws ServiceException {\n");
     sb.append(" \n");
     sb.append("ComposantsMetiersAffichageESIProxy proxy = null;\n");
     sb.append("\n");
     sb.append(" if(NUM != null){\n");
     sb.append("   \n");
     sb.append("   ServiceCriteria criteria = createDefaultServiceCriteria();  \n");
     sb.append("   criteria.addCriterion(ComposantsMetiersAffichagePatrimoinePrimaryKeyServiceCriteria.TYPE_COMPOSANT_CRITERION_NAME,\n"); 
     sb.append("                         ULISConstantes.COMPOSANT_METIER_ESI);\n");
     sb.append("   criteria.addCriterion(ComposantsMetiersAffichagePatrimoinePrimaryKeyServiceCriteria.PAESI_NUM_CRITERION_NAME, NUM);\n");
     sb.append("    \n");
     sb.append("    try {\n");
     sb.append("      \n");
     sb.append("      ServiceResponse serviceResponse = \n");
     sb.append("       resolveServiceResponseByPrimaryKey(cnx, ComposantsMetiersAffichageService.SERVICE_NAME, serviceContext, criteria);\n");
     sb.append("        \n");
     sb.append("      if (! serviceResponse.hasErrors()) {        \n");
     sb.append("        proxy = (ComposantsMetiersAffichageESIProxy) serviceResponse.getElement();\n");
     sb.append("      } else {\n");
     sb.append("        //erreur lors de l'appel du service imbriqué, on lance une ServiceException\n");
     sb.append("        ServiceException servEx = new ServiceException(\"L'appel du service métier \" + ComposantsMetiersAffichageService.SERVICE_NAME\n");
     sb.append("                                                       + \" avec les critères \" + criteria.toString()\n");
     sb.append("                                                       + \" a généré des erreurs \" + serviceResponse.getErrors() );\n");
     sb.append("        if (isLoggingError()) {\n");
     sb.append("           logError(servEx);\n");
     sb.append("        }\n");
     sb.append("        throw servEx;\n");
     sb.append("       }\n");
     sb.append("      \n");
     sb.append("    } catch (ServiceObjectNotFoundException ex){\n");
     sb.append("      proxy = null;\n");
     sb.append("    }//try {\n");
     sb.append("    \n");
     sb.append("  }//if(COD != null){\n");
     sb.append("  \n");
     sb.append("  return proxy;\n");
     sb.append(" }\n");
     return sb.toString();
   }
   private static String declare_ComposantMetierESO(){
     StringBuilder sb = new StringBuilder();
     sb.append(" /**\n");
     sb.append(" * @param cnx Connection\n");
     sb.append("  * @param serviceContext contexte utilisateur\n");
     sb.append("  * @param COD le code\n");
     sb.append("  * @return l'information\n");
     sb.append("  * @throws ServiceException exception\n");
     sb.append("  */\n");
     sb.append(" private ComposantsMetiersAffichageESOProxy _ComposantMetierESO(Connection cnx,\n");
     sb.append("                                                                             ServiceContext serviceContext,\n");
     sb.append("                                                                              String COD)\n");
     sb.append(" throws ServiceException {\n");
     sb.append(" \n");
     sb.append("ComposantsMetiersAffichageESOProxy proxy = null;\n");
     sb.append("\n");
     sb.append(" if(COD != null && !COD.equalsIgnoreCase(\"\")){\n");
     sb.append("   \n");
     sb.append("   ServiceCriteria criteria = createDefaultServiceCriteria();  \n");
     sb.append("   criteria.addCriterion(ComposantsMetiersAffichageESOPrimaryKeyServiceCriteria.TYPE_COMPOSANT_CRITERION_NAME,\n"); 
     sb.append("                         ULISConstantes.COMPOSANT_METIER_ESO);\n");
     sb.append("   criteria.addCriterion(ComposantsMetiersAffichageESOPrimaryKeyServiceCriteria.TOESO_COD_CRITERION_NAME, COD);\n");
     sb.append("    \n");
     sb.append("    try {\n");
     sb.append("      \n");
     sb.append("      ServiceResponse serviceResponse = \n");
     sb.append("       resolveServiceResponseByPrimaryKey(cnx, ComposantsMetiersAffichageService.SERVICE_NAME, serviceContext, criteria);\n");
     sb.append("        \n");
     sb.append("      if (! serviceResponse.hasErrors()) {        \n");
     sb.append("        proxy = (ComposantsMetiersAffichageESOProxy) serviceResponse.getElement();\n");
     sb.append("      } else {\n");
     sb.append("        //erreur lors de l'appel du service imbriqué, on lance une ServiceException\n");
     sb.append("        ServiceException servEx = new ServiceException(\"L'appel du service métier \" + ComposantsMetiersAffichageService.SERVICE_NAME\n");
     sb.append("                                                       + \" avec les critères \" + criteria.toString()\n");
     sb.append("                                                       + \" a généré des erreurs \" + serviceResponse.getErrors() );\n");
     sb.append("        if (isLoggingError()) {\n");
     sb.append("           logError(servEx);\n");
     sb.append("        }\n");
     sb.append("        throw servEx;\n");
     sb.append("       }\n");
     sb.append("      \n");
     sb.append("    } catch (ServiceObjectNotFoundException ex){\n");
     sb.append("      proxy = null;\n");
     sb.append("    }//try {\n");
     sb.append("    \n");
     sb.append("  }//if(COD != null){\n");
     sb.append("  \n");
     sb.append("  return proxy;\n");
     sb.append(" }\n");
     return sb.toString();
   }
	private static String declareExportBI(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Retourne un booléen permettant de savoir si le service réalise un export BI.\n");
    sb.append(" * @return <code>true</code> si le service permet l'export BI, <code>false</code> sinon.\n");
    sb.append(" * @since Kernel 6.0\n");
    sb.append(" */\n");
    sb.append("@Override\n");
    sb.append("public boolean implementsExportBI(){\n");
    sb.append("  return true;\n");
    sb.append("}   \n");
    sb.append("\n");
    sb.append("/**\n");
    sb.append("  * @see sopra.immobilier.service.business.AbstractSQLService#processCreate(java.sql.Connection, sopra.immobilier.service.business.ServiceContext, sopra.immobilier.service.business.ServiceParameters)\n");
    sb.append("  */\n");
    sb.append("@Override\n");
    sb.append(" protected ServiceResponse processCreate(Connection cnx,\n");
    sb.append("                                         ServiceContext context,\n");
    sb.append("                                         ServiceParameters parameters)throws ServiceException,ServiceDuplicateObjectException {\n");
    sb.append("   \n");
    sb.append("   ExportBI"+name+"CreateService service = new ExportBI"+name+"CreateService();\n");
    sb.append("   return service.processExport(cnx, context, parameters);\n");
    sb.append("}  \n");
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.business.ULIS.referentiel.AbstractLanceurExportBIService#getBIDTP_NUM()\n");
    sb.append(" */\n");
    sb.append(" @Override\n");
    sb.append(" protected String getBIDTP_NUM() {\n");
    sb.append("   return \"TOPMI001XXX\";\n");
    sb.append(" }\n");
    sb.append(" \n");
    sb.append(" /**\n");
    sb.append("  * @see sopra.immobilier.business.ULIS.referentiel.AbstractLanceurExportBIService#getWORK_TABLE_NAME()\n");
    sb.append("  */\n");
    sb.append(" @Override\n");
    sb.append(" protected String getWORK_TABLE_NAME() {\n");
    sb.append("   return \"TOXL0XXX\";\n");
    sb.append(" }  \n");
    return sb.toString();
  }
	
	private static String declareServiceupdateRsFromServiceParameters(
			ArrayList<DataTypeMandatory> dataList, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("    /**\n");
		sb.append("     * @param serviceParameters ensemble des valeurs qui permettront de sélectionner l'objet\n");
		sb.append("     * @param rs ResultSet é mettre é jour\n");
		sb.append("     * métier (i.e. clé primaire)\n");
		sb.append("     * compte par exemple les droits d'accés\n");
		sb.append("     * @throws SQLException soulevée si erreur de connexion en base\n");
		sb.append("     * @throws ServiceConcurrentUpdateException soulevée si erreur de connexion en base\n");
		sb.append("     */\n");
		sb.append("    private void updateRsFromServiceParameters(").append(tableName).append("BaseServiceParameters serviceParameters,\n");
		sb.append("                                               ResultSet rs)\n");
		sb.append("                                               throws SQLException,\n");
		sb.append("                                               ServiceConcurrentUpdateException {\n");
		sb.append("        //Mise é jour du ResultSet é partir du serviceParameters\n");
		//On boucle sur chaque paramétre pour mettre é jour ou créer les  objets
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			sb.append(dataTypeMandatory.insertDataInResultSet());
		}
		sb.append("\n");

		sb.append("    }//fin updateRsFromServiceParameters\n");
		return sb.toString();
	}

	private static String declareServiceProcessCreate(
			ArrayList<DataTypeMandatory> dataList, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("    /**\n");
		sb.append("     * Permet de créer un nouvel ensemble de données.\n");
		sb.append("     * @param cnx connexion transactionnelle au sein de laquelle la création devra\n");
		sb.append("     * étre entreprise\n");
		sb.append("     * @param context contexte permettant d'accéder au service et de prendre en\n");
		sb.append("     * compte par exemple les droits d'accés\n");
		sb.append("     * @param parameters ensemble des valeurs qui permettront de construire un\n");
		sb.append("     * nouvel objet métier\n");
		sb.append("     * @return une réponse contenant un et un seul élément correspondant é l'objet\n");
		sb.append("     * métier créé si aucune erreur fonctionnelle n'a été rencontrée\n");
		sb.append("     * @throws ServiceDuplicateObjectException soulevée si l'opération de création\n");
		sb.append("     * ne peut aboutir car l'objet métier existe déjé (contrainte d'unicité sur les\n");
		sb.append("     * attributs identifiant l'objet de faéon non équivoque)\n");
		sb.append("     * @throws ServiceException soulevée é chaque appel pour indiquer que la méthode\n");
		sb.append("     * n'est pas disponible au sein de ce service\n");
		sb.append("     *\n");
		sb.append("     * @see #processCreate(ServiceContext, ServiceParameters)\n");
		sb.append("     */\n");
		sb.append("    @Override\n");
		sb.append("    protected ServiceResponse processCreate(Connection cnx,\n");
		sb.append("        ServiceContext context, ServiceParameters parameters)\n");
		sb.append("        throws ServiceException, ServiceDuplicateObjectException {\n");
		sb.append("      \n");
		sb.append("      ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
		sb.append("      ").append(tableName).append("BaseServiceParameters serviceParameters = new ").append(tableName).append("BaseServiceParameters(parameters);\n");
		sb.append("      \n");
		//On vérifie la présence des paramétres de la clé primaire pour la recherche
		//On déclare les paramétres
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			if(dataTypeMandatory.isKey()){
				sb.append(dataTypeMandatory.checkParameterIsEmpty());
			}
		}
		sb.append("\n");
		sb.append("      if(!serviceResponse.hasErrors()){\n");
		sb.append("        serviceResponse = update").append(tableName).append("Base(cnx, context, serviceParameters, serviceResponse,false);\n");
		sb.append("      }\n");
		sb.append("      \n");
		sb.append("      return serviceResponse;\n");

		sb.append("  }//fin processCreate\n");
		return sb.toString();
	}

	private static CharSequence declareServiceUpdateTABLENAMEBase(
			ArrayList<DataTypeMandatory> dataList, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("    /**\n");
		sb.append("     * @param cnx connexion de lecture au sein de laquelle sera entreprise la \n");
		sb.append("     * recherche\n");
		sb.append("     * @param serviceParameters ensemble des valeurs qui permettront de sélectionner l'objet\n");
		sb.append("     * métier (i.e. clé primaire)\n");
		sb.append("     * @param context contexte permettant d'accéder au service et de prendre en\n");
		sb.append("     * compte par exemple les droits d'accés\n");
		sb.append("     * @param serviceResponse é compléter\n");
		sb.append("     * @param modification doit étre é true si on modifie les données ou é false si on crée\n");
		sb.append("     * @throws ServiceObjectNotFoundException soulevée si l'objet recherché\n");
		sb.append("     * n'existe pas\n");
		sb.append("     * @throws ServiceException soulevée si erreur de connexion en base\n");
		sb.append("     * @throws ServiceConcurrentUpdateException soulevée si erreur de connexion en base\n");
		sb.append("     * @return serviceResponse mis é jour\n");
		sb.append("     */\n");
		sb.append("    private ServiceResponse update").append(tableName).append("Base(Connection cnx,\n");
		sb.append("                                    ServiceContext context,\n");
		sb.append("                                    ").append(tableName).append("BaseServiceParameters serviceParameters,\n");
		sb.append("                                    ServiceResponse serviceResponse,\n");
		sb.append("                                    boolean modification)\n");
		sb.append("                                    throws ServiceObjectNotFoundException,\n");
		sb.append("                                    ServiceConcurrentUpdateException,\n");
		sb.append("                                    ServiceException {\n");
		sb.append("      ServiceResponse newServiceResponse = null;\n");
		sb.append("      if(serviceResponse == null){\n");
		sb.append("        newServiceResponse = createDefaultServiceResponse();\n");
		sb.append("      }else{\n");
		sb.append("        newServiceResponse = serviceResponse;\n");
		sb.append("      }\n");
		sb.append("      StringBuilder sb = new StringBuilder(_SQL_SELECT_CLAUSE)\n");
		sb.append("                                          .append(_SQL_FROM_CLAUSE)\n");
		sb.append("                                          .append(_SQL_PRIMARY_KEY_WHERE_CLAUSE);\n");
		sb.append("      PreparedStatement stmt = null; \n");
		sb.append("      ResultSet rs = null;\n");
		sb.append("      try{\n");
		sb.append("        stmt = cnx.prepareStatement(sb.toString(),ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);\n");
		//On boucle sur chaque paramétre pour mettre é jour ou créer les  objets
		int index =1;
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			if(dataTypeMandatory.isKey()){
				sb.append(dataTypeMandatory.insertCriteriaStatementForSQLWHEREClauseForUpdate(index));
				index= index+1;
			}
		}
		sb.append("        rs = stmt.executeQuery(); \n");
		sb.append("        if(modification){\n");
		sb.append("          if (rs.next()) {// On fait la mise é jour\n");
		sb.append("            rs.moveToCurrentRow();\n");
		sb.append("            updateRsFromServiceParameters(serviceParameters, rs);\n");
		sb.append("            \n");
		sb.append("            // On marque ensuite la mise é jour en vérifiant que l'on ne sait pas fait\n");
		sb.append("            // voler les données par un autre utilisateur\n");
		sb.append("            rs = markRowVersion(rs, context, serviceParameters);\n");
		sb.append("  \n");
		sb.append("            // On transfére les données modifiées au sein de la base de données\n");
		sb.append("            rs.updateRow();\n");
		sb.append("            ").append(tableName).append("Base base = new ").append(tableName).append("Base();\n");
		sb.append("            base = computeBusinessObjectProxyFromResultSet(rs, base);\n");
		sb.append("            newServiceResponse = addElement(newServiceResponse, base);                           \n");
		sb.append("     \n");
		sb.append("          } else {  \n");
		sb.append("            // Tentative de mise é jour d'un élément qui n'existe plus au sein\n");
		sb.append("            // de la base de données\n");
		sb.append("            ServiceObjectNotFoundException notFoundEx = new ServiceObjectNotFoundException(\"La demande de mise é jour n'existe pas au sein de la base de données.\"); \n");
		sb.append("            if (isLoggingError())\n");
		sb.append("                logError(notFoundEx);\n");
		sb.append("            throw notFoundEx;\n");
		sb.append("          }\n");
		sb.append("        }else{\n");
		sb.append("          if (rs.next()) {\n");
		sb.append("            // l'ensemble de données existe déjé, on ne peut étre en création\n");
		sb.append("            newServiceResponse = addError(serviceResponse, new ServiceError(\"Regroupement existant déjé\"));\n");
		sb.append("          }else{\n");
		sb.append("            rs.moveToInsertRow();\n");
		sb.append("            updateRsFromServiceParameters(serviceParameters, rs);\n");
		sb.append("            \n");
		sb.append("            // On marque ensuite la mise é jour en vérifiant que l'on ne sait pas fait\n");
		sb.append("            // voler les données par un autre utilisateur\n");
		sb.append("            rs = markRowVersion(rs, context, serviceParameters);\n");
		sb.append("            \n");
		sb.append("            // On transfére les données modifiées au sein de la base de données\n");
		sb.append("            rs.insertRow();\n");
		sb.append("            ").append(tableName).append("Base base = new ").append(tableName).append("Base();\n");
		sb.append("            base = computeBusinessObjectProxyFromResultSet(rs, base);\n");
		sb.append("            newServiceResponse = addElement(newServiceResponse, base);                                    \n");
		sb.append("          }\n");
		sb.append("        }\n");
		sb.append("      \n");
		sb.append("      }catch (SQLException databaseEx) {\n");
		sb.append("        // Exception soulevée par l'accés é la couche de persistance\n");
		sb.append("        ServiceException ioEx = new ServiceException(\"Erreur technique lors de l'exécution de la requéte en base\",databaseEx);\n");
		sb.append("        if (isLoggingError()) {\n");
		sb.append("          logError(databaseEx);\n");
		sb.append("        }\n");
		sb.append("        throw ioEx;       \n");
		sb.append("      }\n");
		sb.append("      finally {\n");
		sb.append("        // On ferme l'ensemble des ressources que nous avons ouvertes au sein \n");
		sb.append("        // de la base de données\n");
		sb.append("        if (rs != null) {\n");
		sb.append("          try {\n");
		sb.append("            rs.close();\n");
		sb.append("          }\n");
		sb.append("          catch (SQLException ignoreEx) {\n");
		sb.append("            // Exception soulevée par l'accés é la couche de persistance\n");
		sb.append("            ServiceException ioEx = new ServiceException(\"Erreur technique lors de la libération connexion\",ignoreEx);\n");
		sb.append("            if (isLoggingError()) {\n");
		sb.append("              logError(ignoreEx);\n");
		sb.append("            }\n");
		sb.append("            throw ioEx; \n");
		sb.append("          }\n");
		sb.append("          rs = null;\n");
		sb.append("        }\n");
		sb.append("        //\n");
		sb.append("        if (stmt != null) {\n");
		sb.append("          try {\n");
		sb.append("            stmt.close();\n");
		sb.append("          }\n");
		sb.append("          catch (SQLException ignoreEx) {\n");
		sb.append("            // Exception soulevée par l'accés é la couche de persistance\n");
		sb.append("            ServiceException ioEx = new ServiceException(\"Erreur technique lors de la libération connexion\",ignoreEx);\n");
		sb.append("            if (isLoggingError()) {\n");
		sb.append("              logError(ignoreEx);\n");
		sb.append("            }\n");
		sb.append("            throw ioEx; \n");
		sb.append("          }\n");
		sb.append("          stmt = null;\n");
		sb.append("        }\n");
		sb.append("      }\n");
		sb.append("      return newServiceResponse;\n");
		sb.append("    }// fin update").append(tableName).append("Base\n");
		return sb.toString();
	}


	private static String declareServiceProcessUpdate(
			ArrayList<DataTypeMandatory> dataList, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("  /**\n");
		sb.append("   * Permet de mettre é jour la table ").append(tableName).append("\n");
		sb.append("   * @param cnx connexion de lecture au sein de laquelle sera entreprise la \n");
		sb.append("   * recherche\n");
		sb.append("   * @param parameters ensemble des valeurs qui permettront de sélectionner l'objet\n");
		sb.append("   * métier (i.e. clé primaire)\n");
		sb.append("   * @param context contexte permettant d'accéder au service et de prendre en\n");
		sb.append("   * compte par exemple les droits d'accés\n");
		sb.append("   * @return une réponse contenant un et un seul élément correspondant é l'objet\n");
		sb.append("   * métier sélectionné si aucune erreur fonctionnelle n'a été rencontrée\n");
		sb.append("   * @throws ServiceObjectNotFoundException soulevée si l'objet recherché\n");
		sb.append("   * n'existe pas\n");
		sb.append("   * @throws ServiceException soulevée si une erreur technique ne permet pas\n");
		sb.append("   * d'extraire les informations de la couche de persistance\n");
		sb.append("   */\n");
		sb.append("    @Override\n");
		sb.append("    protected ServiceResponse processUpdate(Connection cnx,\n");
		sb.append("                                            ServiceContext context,\n");
		sb.append("                                            ServiceParameters parameters)\n");
		sb.append("                                            throws ServiceException,\n");
		sb.append("                                            ServiceConcurrentUpdateException {\n");
		sb.append("      ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
		sb.append("      ").append(tableName).append("BaseServiceParameters serviceParameters = new ").append(tableName).append("BaseServiceParameters(parameters);\n");
		sb.append("      \n");
		
		//On vérifie la présence des paramétres de la clé primaire pour la recherche
		//On déclare les paramétres
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			if(dataTypeMandatory.isKey()){
				sb.append(dataTypeMandatory.checkParameterIsEmpty());
			}
		}
		sb.append("\n");

		sb.append("      \n");
		sb.append("      if(!serviceResponse.hasErrors()){\n");
		sb.append("        serviceResponse = update").append(tableName).append("Base(cnx, context, serviceParameters, serviceResponse, true);\n");
		sb.append("      }\n");
		sb.append("      return serviceResponse;\n");
		sb.append("    }\n");
		return sb.toString();
	}

	private static String declareServiceParameters(
			ArrayList<DataTypeMandatory> dataList, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("/**\n");
		sb.append("     * Paramétres de création, mise é jour ou suppression\n");
		sb.append("     *\n");
		sb.append("     * <TABLE>\n");
		sb.append("     *   <TR>\n");
		sb.append("     *     <TD><B>Attributs PVCS :</B></TD>\n");
		sb.append("     *     <TD>&nbsp;</TD>\n");
		sb.append("     *   </TR>\n");
		sb.append("     *   <TR>\n");
		sb.append("     *     <TD><I>Nom du fichier :</I></TD>\n");
		sb.append("     *     <TD>$Archive:   O:/Kernel6/archives/workflow/src/main/java/sopra/immobilier/workflow/service/AbstractActeurAffecteService.java-arc  $</TD>\n");
		sb.append("     *   </TR>\n");
		sb.append("     *   <TR>\n");
		sb.append("     *     <TD><I>Modifié le :</I></TD>\n");
		sb.append("     *     <TD>$Date: 2010-10-18 18:14:44 +0200 (lun, 18 oct 2010) $</TD>\n");
		sb.append("     *   </TR>\n");
		sb.append("     * </TABLE>\n");
		sb.append("     *\n");
		sb.append("     * @version  : $Revision: 18332 $\n");
		sb.append("     * @author   : SOPRA Group - $Author: dalonso $\n");
		sb.append("     */\n");
		sb.append("    public static class ").append(tableName).append("BaseServiceParameters extends AbstractServiceParametersProxy {\n");
		sb.append("      // Constantes\n");
		sb.append("      /** Version extraite du gestionnaire de configuration */\n");
		sb.append("      @SuppressWarnings(\"hiding\") \n");
		sb.append("      public static final String CLASS_VERSION = \"$Revision: $\";\n");
		//On déclare le constructeur
		sb.append("      /**\n");
		sb.append("       * @param originalServiceParameters paramétres é passer permettant la mise é jour ou création\n");
		sb.append("       */\n");
		sb.append("      public ").append(tableName).append("BaseServiceParameters(ServiceParameters originalServiceParameters) {\n");
		sb.append("        super(originalServiceParameters);\n");
		sb.append("      }\n");
		
		//On déclare les paramétres
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			sb.append(dataTypeMandatory.declareParameters());
		}
		sb.append("\n");
		
		//On déclare les getters
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			sb.append(dataTypeMandatory.declareGetterParametersForService());
		}
		
		sb.append("}//fin ").append(tableName).append("BaseServiceParameters\n");
		return sb.toString();
	}

	private static String declareServiceCriteriaClass(
			List<Criteria> dataList) {
		StringBuilder sb = new StringBuilder();
		sb.append("    public static class ").append(name).append("ServiceCriteria extends AbstractServiceCriteriaProxy {\n");
		sb.append("        // Constantes\n");
		sb.append("        /** Version extraite du gestionnaire de configuration */\n");
		sb.append("        @SuppressWarnings(\"hiding\") \n");
		sb.append("        public static final String CLASS_VERSION = \"$Revision $\";\n");
		for (Criteria critere : dataList) {
		  if (critere.isObligatoire()){
				sb.append(critere.declareCriteria());
		  }
		}
		
		//panier
		//sb.append("/** Nom du critère donnant de la liste du panier (i.e. <code>LISTE_SELECTION</code>) */\n");
		//sb.append("public static final String LISTE_SELECTION_CRITERION_NAME = \"LISTE_SELECTION\";\n");
		
		//On déclare le constructeur du service criteria
		sb.append("\n/**\n");
		sb.append("         * Crée une nouvelle façade sur les critéres permettant de sélection d'un \n");
		sb.append("         * ensemble d'éléments métier lié à un regroupement de facturation\n");
		sb.append("         * @param criteria critéres génériques devant étre masqué par cette façaade\n");
		sb.append("         */\n");
		sb.append("        protected ").append(name).append("ServiceCriteria(ServiceCriteria criteria) {\n");
		sb.append("            super(criteria);\n");
		sb.append("        } \n");
		
		//On boucle sur tous les getters des membres de la clé primaire
		for (Criteria critere : dataList) {
		  if (critere.isObligatoire()){
				sb.append(critere.declareGetterCriteriaForService());
		  }
		}
		
		//sb.append("/**\n");
		//sb.append(" * La liste \n");
		//sb.append(" * @return la valeur positionnée au sein de la propriété\n");
		//sb.append(" */\n");
		//sb.append("public String[] getLISTE_SELECTION() {\n");
		//sb.append("  return (String[]) getCriterionValue(LISTE_SELECTION_CRITERION_NAME);\n");
		//sb.append("}\n");
		
		sb.append("}//fin ").append(name).append("ServiceCriteria\n");
		return sb.toString();
		
		
	}
	
	private static String declareServiceParametersClass(
	                                                    List<Criteria> dataList) {
	  StringBuilder sb = new StringBuilder();
	  sb.append("    public static class ").append(name).append("ServiceParameters extends AbstractServiceParametersProxy {\n");
	  sb.append("        // Constantes\n");
	  sb.append("        /** Version extraite du gestionnaire de configuration */\n");
	  sb.append("        @SuppressWarnings(\"hiding\") \n");
	  sb.append("        public static final String CLASS_VERSION = \"$Revision $\";\n");
	  for (Criteria critere : dataList) {
	    sb.append(critere.declareParameters());
	  }
	  
	  
	  //On déclare le constructeur du service criteria
	  sb.append("\n/**\n");
	  sb.append("         * Crée une nouvelle façade sur les critéres permettant de sélection d'un \n");
	  sb.append("         * ensemble d'éléments métier lié à un regroupement de facturation\n");
	  sb.append("         * @param criteria critéres génériques devant étre masqué par cette façaade\n");
	  sb.append("         */\n");
	  sb.append("        protected ").append(name).append("ServiceParameters(ServiceParameters parameters) {\n");
	  sb.append("            super(parameters);\n");
	  sb.append("        } \n");
	  
	  //On boucle sur tous les getters des membres de la clé primaire
	  for (Criteria critere : dataList) {
	    sb.append(critere.declareGetterParameterForService());
	  }

	  sb.append("}//fin ").append(name).append("ServiceParameter\n");
	  return sb.toString();
	  
	  
	}

	private static String declareServiceCriteriaCommentary() {
		StringBuilder sb = new StringBuilder();
		sb.append("    /**\n");
		sb.append("     * Critéres de recherche ").append(name).append("\n");
		sb.append("     *\n");
		sb.append("     * <TABLE>\n");
		sb.append("     *   <TR>\n");
		sb.append("     *     <TD><B>Attributs SVN :</B></TD>\n");
		sb.append("     *     <TD>&nbsp;</TD>\n");
		sb.append("     *   </TR>\n");
		sb.append("     *   <TR>\n");
		sb.append("     *     <TD><I>Nom du fichier :</I></TD>\n");
		sb.append("     *     <TD>$HeadURL: http://svn.lyon.fr.sopra/svnulis/svn/ulis_test/repository/Refonte/ULISNG/ $</TD>\n");
		sb.append("     *   </TR>\n");
		sb.append("     *   <TR>\n");
		sb.append("     *     <TD><I>Modifié le :</I></TD>\n");
		sb.append("     *     <TD>$Date: 2010-10-18 18:14:44 +0200 (lun, 18 oct 2010) $</TD>\n");
		sb.append("     *   </TR>\n");
		sb.append("     * </TABLE>\n");
		sb.append("     *\n");
		sb.append("     * @version  : $Revision: 18332 $\n");
		sb.append("     * @author   : SOPRA Group - $Author: $\n");
		sb.append("     *\n");
		sb.append("     */\n");
		return sb.toString();
	}
	private static String declareServiceParametersCommentary() {
    StringBuilder sb = new StringBuilder();
    sb.append("    /**\n");
    sb.append("     * parametres ").append(name).append("\n");
    sb.append("     *\n");
    sb.append("     * <TABLE>\n");
    sb.append("     *   <TR>\n");
    sb.append("     *     <TD><B>Attributs SVN :</B></TD>\n");
    sb.append("     *     <TD>&nbsp;</TD>\n");
    sb.append("     *   </TR>\n");
    sb.append("     *   <TR>\n");
    sb.append("     *     <TD><I>Nom du fichier :</I></TD>\n");
    sb.append("     *     <TD>$HeadURL: http://svn.lyon.fr.sopra/svnulis/svn/ulis_test/repository/Refonte/ULISNG/ $</TD>\n");
    sb.append("     *   </TR>\n");
    sb.append("     *   <TR>\n");
    sb.append("     *     <TD><I>Modifié le :</I></TD>\n");
    sb.append("     *     <TD>$Date: 2010-10-18 18:14:44 +0200 (lun, 18 oct 2010) $</TD>\n");
    sb.append("     *   </TR>\n");
    sb.append("     * </TABLE>\n");
    sb.append("     *\n");
    sb.append("     * @version  : $Revision: 18332 $\n");
    sb.append("     * @author   : SOPRA Group - $Author: $\n");
    sb.append("     *\n");
    sb.append("     */\n");
    return sb.toString();
  }
	private static String declareServiceNameVersionDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n  /**\n");
		sb.append("   * Retourne le nom du service métier\n");
		sb.append("   * @return la valeur positionnée au sein de la propriété\n");
		sb.append("   */\n");
		sb.append("  public String getName() {\n");
		sb.append("    return SERVICE_NAME;\n");
		sb.append("  }\n");
		sb.append("  /**\n");
		sb.append("   * Retourne une description textuelle du service métier\n");
		sb.append("   * @return la valeur positionnée au sein de la propriété\n");
		sb.append("   */\n");
		sb.append("  public String getDescription() {\n");
		sb.append("    return SERVICE_DESCRIPTION;\n");
		sb.append("  }\n");
		sb.append("  /**\n");
		sb.append("   * Retourne la version du service métier sous forme de chaéne de caractéres\n");
		sb.append("   * @return la valeur positionnée au sein de la propriété\n");
		sb.append("   */\n");
		sb.append("  public String getVersion() {\n");
		sb.append("    return CLASS_VERSION;\n");
		sb.append("  }\n\n");
		return sb.toString();
	}
	
	private static String declareServiceClassConstructor() {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Crée un service\n");
    sb.append(" */\n");
    sb.append("public "+name+"Service() {\n");
    sb.append("  super();\n");
    sb.append("}\n");
    return sb.toString();
  }

	private static String declareServiceClassSQLClause() {
		StringBuilder sb = new StringBuilder();
		sb.append("  /** Clause SELECT d'accés é la table */\n");
		
		boolean selectFind=false;
		String select=null;
		boolean fromFind=false;
		String from=null;
		boolean whereFind=false;
		String where=null;
		
		for (Criteria criteria: listeCriteria){
      if (criteria.getSelect()!=null && !criteria.getSelect().equals("") && !selectFind){
        select=criteria.getSelect();
        selectFind=true;
      }
      if (criteria.getFrom()!=null && !criteria.getFrom().equals("") && !fromFind){
        from=criteria.getFrom();
        fromFind=true;
      }
      if (criteria.getWhere()!=null && !criteria.getWhere().equals("") && !whereFind){
        where=criteria.getWhere();
        whereFind=true;
      }
    }
		
		sb.append("/** Requête principal. SELECT */\n");
		if (selectFind){sb.append("private static final String _SQL_PRINCIPAL_SELECT     = \" "+select+" \";\n");}
		else{sb.append("private static final String _SQL_PRINCIPAL_SELECT     = \"SELECT distinct \";\n");}
    
		sb.append("/** Requête principal. FROM */\n");
    if (fromFind){sb.append("private static final String _SQL_PRINCIPAL_FROM       = \" "+from+" \"; \n");}
    else{sb.append("private static final String _SQL_PRINCIPAL_FROM       = \"FROM \"; \n");}
    
    sb.append("/** Requête principal. WHERE */\n");
    if (whereFind){sb.append("private static final String _SQL_PRINCIPAL_WHERE      = \" "+where+" \";\n");}
    else{sb.append("private static final String _SQL_PRINCIPAL_WHERE      = \" WHERE 1=1 \";\n");}
		
		sb.append("/** Requête pour le nombre des ESI vacants */\n");
		sb.append("private static final String _SQL_PRINCIPAL_COUNT      = \"SELECT COUNT(*) \";\n");
		sb.append("\n");
		return sb.toString();
	}
	
	 private static String declareServiceClassSQLClauseCriteria(List<Criteria> dataListe) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("  /** Clause SELECT d'accés é la table */\n");
      for (Criteria critere : dataListe) {
        sb.append("/** Ajout de critère "+critere.getNom()+" :"+critere.getDescription()+", pour la requête principal. */\n");
        sb.append("private static final String _SQL_"+critere.getNom().toUpperCase()+"           = \" AND "+critere.getNom()+" = ? \";\n");
      }
	    return sb.toString();
	  }
	 
	 private static String declareServiceClassSQLOrder(List<Proxy> dataListeProxy) {
	 
	   StringBuilder sb = new StringBuilder();
	   
	   if (isMultiOccurence()){
	     sb.append(" /** Nom du propiete pour le tri par défaut */\n");
	     
	     //recuperation du premier champs triable
	     sb.append("private static final String _ORDER_BY_DEFAULT = \"");
	     for (Proxy critere : dataListeProxy) {
	       if (critere.isTriable()){
	         sb.append(critere.getNom());
	         break;
	       } 
	     }
	     sb.append("\";\n");
	     sb.append("\n");
	     sb.append("/** Noms des différents tris exposés par le service métier */\n");
	     sb.append("private static final String[] _ORDER_PROPERTIES = new String[] {");
	     int index=0;
	     for (Proxy critere : dataListeProxy) {
	       if (critere.isTriable()){
	         if (index>0){sb.append(",");}
	         index++;
	         sb.append("\""+critere.getNom()+"\"");
	       } 
	     }
	     sb.append("};\n");
	     sb.append("		\n");
	     sb.append("/** Noms des couples de colonnes permettant d'entreprendre le tri */\n");
	     sb.append("private static final String[] _ORDER_COLUMN_PROPERTIES = new String[] {");
	     int index2=0;
	     for (Proxy critere : dataListeProxy) {
	       if (critere.isTriable()){
	         if (index2>0){sb.append(",");}
	         index2++;
	         sb.append("\""+critere.getNom()+"\"");
	       }
	     }
	     sb.append("};\n");
	   }
	   return sb.toString();
	 }

	 private static String declareServiceClassSQLFilter(List<Proxy> dataListeProxy) {
	   
     StringBuilder sb = new StringBuilder();
     if (isMultiOccurence()){
       sb.append(" /** Nom du propiete pour le tri par défaut */\n");
       
       sb.append("public static Map<String, String>  _champsFiltres = new HashMap<String, String>();\n");
       sb.append("static{\n");   
       for (Proxy critere : dataListeProxy) {
         if (critere.isFiltrable()){
           sb.append("_champsFiltres.put(\""+critere.getNom()+"\", \""+critere.getNom()+"\");\n"); 
           break;
         } 
       }
       sb.append("  \n");       
       sb.append(" }\n");       
     }
     
     return sb.toString();
   }

	 
	private static String declareServiceClassCommentary() {
		StringBuilder sb = new StringBuilder();
		sb.append("/**\n");
		sb.append(" * Description :\n");
		sb.append(" * Service ").append(name+"Service").append("\n");
		sb.append(" *\n");
		sb.append(" * <TABLE>\n");
		sb.append(" *   <TR>\n");
		sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
		sb.append(" *     <TD>&nbsp;</TD>\n");
		sb.append(" *   </TR>\n");
		sb.append(" *   <TR>\n");
		sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
		sb.append(" *     <TD>$HeadURL: http://svn.lyon.fr.sopra/svnulis/svn/ulis_test/repository/Refonte/ULISNG/ $</TD>\n");
		sb.append(" *   </TR>\n");
		sb.append(" *   <TR>\n");
		sb.append(" *     <TD><I>Modifié le :</I></TD>\n");
		sb.append(" *     <TD>$Date: 2010-10-18 18:14:44 +0200 (lun, 18 oct 2010) $</TD>\n");
		sb.append(" *   </TR>\n");
		sb.append(" * </TABLE>\n");
		sb.append(" *\n");
		sb.append(" * @version : $Revision: 18332 $\n");
		sb.append(" * @author  : SOPRA Group - $Author: Gthomazeau $\n");
		sb.append(" *\n");
		sb.append(" */\n");
		return sb.toString();
	}

	private static String declareServiceClassDeclaration() {
		StringBuilder sb = new StringBuilder();
		sb.append("public class ").append(name+"Service").append(" extends AbstractULISSQLService {\n");
		sb.append("  //Constantes\n");
		sb.append("  /** Version extraite du gestionnaire de configuration */\n");
		sb.append("  @SuppressWarnings(\"hiding\")\n");
		sb.append("  public static final String CLASS_VERSION = \"$Revision: $\";\n");
		sb.append("  //Attributs\n");
		sb.append("		  /** Nom logique du service */\n");
		sb.append("		  public static final String SERVICE_NAME = \"referentiel.").append(packageName).append(".").append(name).append("Service\";\n");
		sb.append("		  /** Description textuelle du service */\n");
		sb.append("		  public static final String SERVICE_DESCRIPTION = \"Service ").append(name+"Service").append("\";\n");
		return sb.toString();
	}
	
	private static String declareServiceClassDeclarationForSynthesis() {
    StringBuilder sb = new StringBuilder();
    sb.append("public class ").append(name+"Service").append(" extends AbstractULISSQLService {\n");
    sb.append("  //Constantes\n");
    sb.append("  /** Version extraite du gestionnaire de configuration */\n");
    sb.append("  @SuppressWarnings(\"hiding\")\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision: $\";\n");
    sb.append("  //Attributs\n");
    sb.append("     /** Nom logique du service */\n");
    sb.append("     public static final String SERVICE_NAME = \"referentiel").append(packageName).append(".").append(name).append("Service\";\n");
    sb.append("     /** Description textuelle du service */\n");
    sb.append("     public static final String SERVICE_DESCRIPTION = \"Service ").append(name+"Service").append("\";\n");
    return sb.toString();
  }

	private static String declareServiceImportServiceClass(List<Criteria> dataList) {
		StringBuilder sb = new StringBuilder();		
		sb.append("import java.sql.Connection;\n");
		sb.append("import java.sql.PreparedStatement;\n");
		sb.append("import java.sql.ResultSet;\n");
		sb.append("import java.sql.SQLException;\n");
		sb.append("import java.util.ArrayList;\n");
		sb.append("import java.util.List;\n");
		sb.append("import java.util.HashMap;\n");
		sb.append("import java.util.Map;\n");
		sb.append("import sopra.core.util.DateTools;\n");
		sb.append("import sopra.immobilier.business.ULIS.referentiel.CodificationValeurService;\n");
		sb.append("import sopra.immobilier.business.ULIS.referentiel.CodificationValeurService.CodificationValeurServiceCriteria;\n");
		sb.append("import sopra.immobilier.business.ULIS.AbstractULISSQLService;\n");
		sb.append("import sopra.immobilier.business.ULIS.SQLCriteria;\n");
		sb.append("import sopra.immobilier.business.ULIS.referentiel.CodificationValeurProxy;\n");
		sb.append("import sopra.immobilier.business.ULIS.referentiel.CodificationValeurService;\n");
		sb.append("import sopra.immobilier.service.business.AbstractServiceCriteriaProxy;\n");
		sb.append("import sopra.immobilier.service.business.ServiceContext;\n");
		sb.append("import sopra.immobilier.service.business.ServiceCriteria;\n");
		sb.append("import sopra.immobilier.service.business.ServiceException;\n");
		sb.append("import sopra.immobilier.service.business.ServiceFindAttributes;\n");
		sb.append("import sopra.immobilier.service.business.ServiceObjectNotFoundException;\n");
		sb.append("import sopra.immobilier.service.business.ServiceResponse;\n");
		sb.append("import sopra.core.annotation.constraints.MaxLengthConstraint;\n");
		
		if(containsCriteria(dataList, "BigDecimal")){
			sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForADecimal;\n");
			sb.append("import java.math.BigDecimal;\n");
		}
		if(containsCriteria(dataList, "Integer")){
			sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForAnInteger;\n");
			sb.append("import javax.validation.constraints.Max;\n");
		}
		if(containsCriteria(dataList, "Date")){
			sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForADate;\n");
			sb.append("import java.util.Date;\n");
		}
		return sb.toString();
	}

	@SuppressWarnings("unused")
  private static List<Proxy> gettingProxyFromFile(String pathIn) {
	  
	  //recuperation du fichier
	  File dir = new File(pathIn);
    FilenameFilter filterTable = new FilenameFilter() {
        public boolean accept(File dir, String nom) {
          return (nom.startsWith(name) && nom.endsWith(".proxy"));
        }
      };
    String[] children = dir.list(filterTable);
    
  
    String nomFichier = children[0]; //on prends seulement un fichier
	  
	  List<Proxy> dataList = new ArrayList<Proxy>();
		try { 
			BufferedReader in = new BufferedReader(new FileReader(pathIn+nomFichier)); 
			String str; 
			
			while ((str = in.readLine()) != null) { 
				Proxy data = new Proxy(str);
				dataList.add(data);
			} 
			in.close(); 
			
		} catch (FileNotFoundException e) {e.printStackTrace(); } 
		catch (IOException ioe){}
		return dataList;	    
	}

	 private static List<Criteria> gettingCriteriaFromFile(String pathIn) {
	    
	    //recuperation du fichier
	    File dir = new File(pathIn);
	    FilenameFilter filterTable = new FilenameFilter() {
	        public boolean accept(File dir, String nom) {
	          return (nom.startsWith(name) && nom.endsWith(".criteria"));
	        }
	      };
	    String[] children = dir.list(filterTable);
	    
	  
	    String nomFichier = children[0]; //on prends seulement un fichier
	    
	    List<Criteria> dataList = new ArrayList<Criteria>();
	    try { 
	      BufferedReader in = new BufferedReader(new FileReader(pathIn+nomFichier)); 
	      String str; 
	      
	      while ((str = in.readLine()) != null) { 
	        Criteria data = new Criteria(str);
	        dataList.add(data);
	      } 
	      in.close(); 
	      
	    } catch (FileNotFoundException e) {e.printStackTrace(); } 
	    catch (IOException ioe){}
	    return dataList;      
	  }
	
	private static void generateProxyClass(String path, String name, List<Criteria> listeCriteria){
		
		try {
			File file = new File(path+name+"Proxy.java");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
			out.append(declarePackage());
			out.append(declareImportProxyClass(listeCriteria));
			out.append(declareProxyClassCommentary(name));
			out.append(declareClasseDeclaration());
			for (Criteria donnee : listeCriteria) {
			  out.append(donnee.declareComposantMetier());
				out.append(donnee.declareConstante());
				out.append("\n");				
			}
			for (Criteria donnee : listeCriteria) {
			  out.append(donnee.declareGetterComposantMetier());
				out.append(donnee.declareGetterForBaseProxy());
				out.append(donnee.declareSetterComposantMetier());
				out.append(donnee.declareSetterForBaseProxy());
				out.append("\n");				
			}
			out.append("}//fin classe ").append(name).append("Proxy");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private static String declareClasseDeclaration() {
		StringBuilder sb = new StringBuilder();
		sb.append("public class ").append(name).append("Proxy extends AbstractBusinessObject {\n");
		sb.append("  //Constantes\n");
		sb.append("  /** Version extraite du gestionnaire de configuration */\n");
		sb.append("  @SuppressWarnings(\"hiding\")\n");
		sb.append("  public static final String CLASS_VERSION = \"$Revision: $\";\n");
		sb.append("  //Attributs\n");
		return sb.toString();
	}

	private static String declareProxyClassCommentary(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n/**\n");
		sb.append(" * Représente un proxy ").append(tableName).append("\n");
		sb.append(" *\n");
		sb.append(" * <TABLE>\n");
		sb.append(" *   <TR>\n");
		sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
		sb.append(" *     <TD>&nbsp;</TD>\n");
		sb.append(" *   </TR>\n");
		sb.append(" *   <TR>\n");
		sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
		sb.append(" *     <TD>$HeadURL:   /GL/src/main/java/sopra/immobilier/business/ULIS/referentiel/").append(tableName).append("Base.java\n");
		sb.append(" *   </TR>\n");
		sb.append(" *   <TR>\n");
		sb.append(" *     <TD><I>Modifié le :</I></TD>\n");
		sb.append(" *     <TD>$Date: 2010-10-18 18:14:44 +0200 (lun, 18 oct 2010) $</TD>\n");
		sb.append(" *   </TR>\n");
		sb.append(" * </TABLE>\n");
		sb.append(" *\n");
		sb.append(" * @version : $Revision: 18332 $\n");
		sb.append(" * @author  : SOPRA Group - $\n");
		sb.append(" *\n");
		sb.append(" * @since Kernel 6.0\n");
		sb.append(" *\n");
		sb.append(" */\n");
		return sb.toString();	
	}

	private static String declareImportProxyClass(List<Criteria> dataList) {
		StringBuilder sb = new StringBuilder();
		sb.append("import sopra.core.annotation.constraints.MaxLengthConstraint;\n");
		sb.append("import sopra.immobilier.business.ULIS.AbstractBusinessObject;\n");
		sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForADate;\n");
		sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForAnInteger;\n");
		sb.append("\nimport sopra.immobilier.business.ULIS.AbstractBusinessObject;\n");
		if(containsCriteria(dataList,"BigDecimal"))
			sb.append("import java.math.BigDecimal;\n");
		if(containsCriteria(dataList,"Date"))
			sb.append("import java.util.Date;\n");
		System.out.println("Don't forget to open the result file on eclipse and check import.");
		return sb.toString();		
	}

	private static String declarePackageForSynthesis() {
		StringBuilder sb = new StringBuilder();
		sb.append("/**\n").append("* Paquet de définition\n").append("*/\n");
		sb.append("package sopra.immobilier.business.ULIS.referentiel."+packageName+"; \n\n");
		return sb.toString();
	}
	
	private static String declarePackage() {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n").append("* Paquet de définition\n").append("*/\n");
    sb.append("package sopra.immobilier.business.ULIS.referentiel."+packageName+"; \n\n");
    return sb.toString();
  }
	
	private static String declarePackageCriteriaBean() {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n").append("* Paquet de définition\n").append("*/\n");
    sb.append("package sopra.immobilier.faces."+packageName+"; \n\n");
    return sb.toString();
  }
	
	private static boolean contains(List<Proxy> dataList, String type){
		boolean found = false;
		Iterator<Proxy> it = dataList.iterator();
		while (it.hasNext()&&(!found)) {
			Proxy donnee = it.next();
			found = donnee.getType().equalsIgnoreCase(type);
		}
		return found;
		
	}
	
	private static boolean containsCriteria(List<Criteria> dataList, String type){
    boolean found = false;
    Iterator<Criteria> it = dataList.iterator();
    while (it.hasNext()&&(!found)) {
      Criteria donnee = it.next();
      found = donnee.getType().equalsIgnoreCase(type);
    }
    return found;
    
  }
	
	private static boolean containsSomme(List<Proxy> dataListProxy){
    boolean found = false;
    Iterator<Proxy> it = dataListProxy.iterator();
    while (it.hasNext()&&(!found)) {
      Proxy donnee = it.next();
      if (donnee.getType().equalsIgnoreCase("BigDecimal") && donnee.isSommable())
      found = true;
    }
    return found;
    
  }
	

	private static void generateCriteriaBeanClass(String path, 
	                                         List<Criteria> dataList) {
	  try {
	    File file = new File(path+name+"Bean.java");
	    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
	    out.append(declarePackageCriteriaBean());
	    out.append(declareServiceImportCriteriaBeanClass(dataList));
	    out.append(declareCriteriaBeanClassCommentary());
	    out.append(declareCriteriaBeanClassDeclaration());
	    //out.append(declareCriteriaBeanClassConstructor());
	    out.append(declareGetterSetter(dataList));

	    out.append("}//fin classe ").append(name).append("Bean");
	    out.close();
	  } catch (Exception e) {
	    //Exception non soulevée
	  } 
	}
	
	private static String declareServiceImportCriteriaBeanClass(List<Criteria> dataList) {
    StringBuilder sb = new StringBuilder();   
    sb.append("import sopra.core.annotation.Criteria;\n");
    sb.append("import sopra.core.annotation.Criterion;\n");
    sb.append("import sopra.core.annotation.constraints.MaxLengthConstraint;\n");
    for (Criteria critere : dataList) {
      if (critere.getListeDeroulante()!=null && critere.getListeDeroulante().getNomRegle()!=null){
        sb.append("import sopra.immobilier.business.ULIS."+packageName+"."+critere.getListeDeroulante().getNomRegle()+"FindService;\n");
      }
    }
    
    sb.append("import sopra.immobilier.business.ULIS."+packageName+"."+name+"Service."+name+"ServiceCriteria;\n");
    sb.append("import sopra.immobilier.business.ULIS.referentiel.ListeDeroulanteService;\n");
    sb.append("import sopra.immobilier.business.ULIS.referentiel.ListeDeroulanteService.ListeDeroulanteServiceCriteria;\n");
    sb.append("import sopra.immobilier.model.ModelBean;\n");
    sb.append("import sopra.immobilier.model.annotation.ListOfValues;\n");
    sb.append("import sopra.immobilier.model.annotation.ServiceFindAttributes;\n");
    sb.append("import sopra.immobilier.model.annotation.ListOfValues.Type;\n");
    sb.append("import sopra.immobilier.service.business.ServiceInputOutputFactory;\n");
    sb.append("import sopra.immobilier.business.ULIS.referentiel.PA.PAESICriterion;\n");
    sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForAnAmount;\n");
    sb.append("import sopra.navigation.AutoInvoke;\n");
    sb.append("import sopra.core.annotation.Update;\n");
    sb.append("import sopra.immobilier.struts.actions.referentiel.ULISConstantes;\n");
    sb.append("import sopra.immobilier.navigation.referentiel.GeneriqueShoppingCartFIFunction;\n");
    sb.append("import sopra.core.annotation.Parameter;\n");
    sb.append("\n");
    if(containsCriteria(dataList, "BigDecimal")){
      sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForADecimal;\n");
      sb.append("import java.math.BigDecimal;\n");
    }
    if(containsCriteria(dataList, "Integer")){
      sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForAnInteger;\n");
      sb.append("import javax.validation.constraints.Max;\n");
    }
    if(containsCriteria(dataList, "Date")){
      sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForADate;\n");
      sb.append("import java.util.Date;\n");
    }
    return sb.toString();
  }
	
	 private static String declareCriteriaBeanClassCommentary() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("/**\n");
	    sb.append(" * Description :\n");
	    sb.append(" * Service ").append(name+"CriteriaBean").append("\n");
	    sb.append(" *\n");
	    sb.append(" * <TABLE>\n");
	    sb.append(" *   <TR>\n");
	    sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
	    sb.append(" *     <TD>&nbsp;</TD>\n");
	    sb.append(" *   </TR>\n");
	    sb.append(" *   <TR>\n");
	    sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
	    sb.append(" *     <TD>$HeadURL: http://svn.lyon.fr.sopra/svnulis/svn/ulis_test/repository/Refonte/ULISNG/ $</TD>\n");
	    sb.append(" *   </TR>\n");
	    sb.append(" *   <TR>\n");
	    sb.append(" *     <TD><I>Modifié le :</I></TD>\n");
	    sb.append(" *     <TD>$Date: 2010-10-18 18:14:44 +0200 (lun, 18 oct 2010) $</TD>\n");
	    sb.append(" *   </TR>\n");
	    sb.append(" * </TABLE>\n");
	    sb.append(" *\n");
	    sb.append(" * @version : $Revision: 18332 $\n");
	    sb.append(" * @author  : SOPRA Group - $Author: Gthomazeau $\n");
	    sb.append(" *\n");
	    sb.append(" */\n");
	    return sb.toString();
	  }
	 
	  private static String declareCriteriaBeanClassDeclaration() {
	    StringBuilder sb = new StringBuilder();
	    
	    sb.append("@ModelBean\n");
	    sb.append("public class ").append(name).append("Bean extends "+name+"Proxy {\n");
	    sb.append("  //Constantes\n");
	    sb.append("  /** Version extraite du gestionnaire de configuration */\n");
	    sb.append("  @SuppressWarnings(\"hiding\")\n");
	    sb.append("  public static final String CLASS_VERSION = \"$Revision: $\";\n");
	    
	    return sb.toString();
	  }
	  
	  private static String declareCriteriaBeanClassConstructor() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("/**\n");
	    sb.append(" * Crée un service\n");
	    sb.append(" */\n");
	    sb.append("public "+name+"CriteriaBean() {\n");
	    sb.append("super(ServiceInputOutputFactory.getInstance().createDefaultServiceCriteria());\n");
	    sb.append("}\n");
	    return sb.toString();
	  }
	  
	  private static String declareGetterSetter(List<Criteria> dataList) {
      StringBuilder sb = new StringBuilder();
      
      for (Criteria critere : dataList) {
        if (critere.getListeDeroulante()!=null || critere.getPanier()!=null ||critere.getFastInput()!=null){
        //pas besoin de declarer le getter dans le criteria bean pour des champs simple
        //GETTER      
        sb.append("/**\n");
        sb.append(" * \n");
        sb.append(" */\n");
        sb.append("  @Override\n");
        sb.append(critere.declareAnnotation());
        
        if (critere.getListeDeroulante()!=null){
          if (critere.getListeDeroulante().getType().equalsIgnoreCase("COD")){
            sb.append("@Criteria({@Criterion(value=\""+critere.getListeDeroulante().getNomCodif()+"\", targetName=ListeDeroulanteServiceCriteria.NOM_CODIF_CRITERION_NAME),\n");
            sb.append("  @Criterion(value=\""+critere.getListeDeroulante().getTypeAffichage()+"\",targetName=ListeDeroulanteServiceCriteria.TYPE_LIBELLE_CRITERION_NAME)})\n");
            sb.append("  @ListOfValues(listName=\"list"+critere.getNom()+"\",\n");
            sb.append("     serviceName=ListeDeroulanteService.SERVICE_NAME,\n");
            sb.append("     serviceFindAttributes=@ServiceFindAttributes(order=\"LIB\"),\n");
            sb.append("     type=Type.LOCAL) \n");
          }
          
          if (critere.getListeDeroulante().getType().equalsIgnoreCase("SPE")){
            sb.append("@Criteria({\n");
              sb.append("@Criterion(value="+critere.getListeDeroulante().getNomRegle()+"FindService.SERVICE_NAME,targetName=ListeDeroulanteServiceCriteria.SERV_SPE_CRITERION_NAME),\n");
              sb.append("@Criterion(value=\""+critere.getListeDeroulante().getTypeAffichage()+"\",targetName=ListeDeroulanteServiceCriteria.TYPE_LIBELLE_CRITERION_NAME)\n");
              sb.append("})\n");
              sb.append("@ListOfValues(listName=\"list"+critere.getNom()+"\", serviceName=ListeDeroulanteService.SERVICE_NAME, type=Type.LOCAL)\n");
          }
          
          if (critere.getListeDeroulante().getType().equalsIgnoreCase("TAB")){
            sb.append("@Criteria({\n");
            sb.append("  @Criterion(value=\""+critere.getListeDeroulante().getNomTable()+"\", targetName=ListeDeroulanteServiceCriteria.NOM_TABLE_CRITERION_NAME),\n");
            sb.append("  @Criterion(value=\""+critere.getListeDeroulante().getNomChampCod()+"\", targetName=ListeDeroulanteServiceCriteria.NOM_CHAMP_COD_CRITERION_NAME),\n");
            if (critere.getListeDeroulante().getNomChampLib()!=null && !critere.getListeDeroulante().getNomChampLib().isEmpty()){
              sb.append("  @Criterion(value=\""+critere.getListeDeroulante().getNomChampLib()+"\", targetName=ListeDeroulanteServiceCriteria.NOM_CHAMP_LIB_CRITERION_NAME),\n");
            }
            if (critere.getListeDeroulante().getNomChampEtat()!=null && !critere.getListeDeroulante().getNomChampEtat().isEmpty()){
              sb.append("  @Criterion(value=\""+critere.getListeDeroulante().getNomChampEtat()+"\", targetName=ListeDeroulanteServiceCriteria.NOM_CHAMP_ETAT_CRITERION_NAME),\n");
              sb.append("  @Criterion(value=ULISConstantes.ACTIF, targetName=ListeDeroulanteServiceCriteria.ETAT_CRITERION_NAME),\n");
            }
            
            sb.append("  @Criterion(value=\""+critere.getListeDeroulante().getTypeAffichage()+"\",targetName=ListeDeroulanteServiceCriteria.TYPE_LIBELLE_CRITERION_NAME)\n");
            sb.append("  })\n");
            sb.append("@ListOfValues(listName=\"list"+critere.getNom()+"\", serviceName=ListeDeroulanteService.SERVICE_NAME, type=Type.LOCAL)\n");
          }
          if (critere.getListeDeroulante().getType().equalsIgnoreCase("MAN")){//ne rien faire traitement dans le XHTML
            
          }
           
        }
        if (critere.getPanier()!=null){
          if (critere.getPanier().getType().equalsIgnoreCase("generique")){
            //gestion d'un panier générique
            //Gestion de la clause
            if (critere.getPanier().getClause()!=null){
              sb.append("  public String getClause() {\n");
              sb.append("    if (get"+critere.getNom()+"()!=null){\n");
              sb.append("      return "+critere.getPanier().getClause()+";\n");
              sb.append("    }\n");
              sb.append("    return null;\n");
              sb.append("  }\n");
            }
            
            sb.append("  @AutoInvoke(name = \"panier"+critere.getNom()+"\", \n");
            sb.append("              function = GeneriqueShoppingCartFIFunction.class, \n");
            sb.append("              usage = AutoInvoke.SHOPPING_CART_USAGE, \n");
            sb.append("              inputParameters = {@Parameter(propertyName = \""+critere.getNom()+"\", targetName=\"generiqueShoppingCartList\"),\n");
            sb.append("                                 @Parameter(targetName = \"nomChampCod\", value = \""+critere.getPanier().getNomChampCod()+"\"),\n");
            sb.append("                                 @Parameter(targetName = \"nomChampLib\", value = \""+critere.getPanier().getNomChampLib()+"\"),\n");
            sb.append("                                @Parameter(targetName = \"nomTable\", value = \""+critere.getPanier().getNomTable()+"\")\n");
            if (critere.getPanier().getNomChampEtat()!=null){
              sb.append("                                ,@Parameter(targetName = \"nomChampEtat\", value = \""+critere.getPanier().getNomChampEtat()+"\")\n");
              sb.append("                                ,@Parameter(targetName = \"ETAT\", value = "+critere.getPanier().getETAT()+"),\n");
            }
            if (critere.getPanier().getClause()!=null){
              sb.append("                                ,@Parameter(targetName = \"clause\", propertyName = \"clause\")");
            }
            sb.append("		}\n");
            sb.append("             ,updates = { @Update(propertyName = \""+critere.getNom()+"\", targetName=\"generiqueShoppingCartList\")})\n");
          }else if (critere.getPanier().getType().equalsIgnoreCase("fonction")){
            //gestion d'un panier appelant une autre fonction
            sb.append("@AutoInvoke(name = \""+critere.getNom()+"\", \n");
            sb.append("            function = "+critere.getPanier().getNomFonction()+".class, \n");
            sb.append("            usage = AutoInvoke.SHOPPING_CART_USAGE, \n");
            sb.append("            updates = { @Update(propertyName = \""+critere.getNom()+"\", targetName=NomDuChampDuContratDeFonctionEnSortie)},\n");
            sb.append("            inputCriteria = { @Criterion(propertyName = \""+critere.getNom()+"\", targetName=NomDuChampDuContratDeFonctionEnEntree)}\n");
            sb.append("            )\n");
          }
        }
        if (critere.getFastInput()!=null){
          sb.append("@AutoInvoke(name = \""+critere.getNom()+"_FastInput\", function = "+critere.getFastInput().getNomFonction()+"Function.class, usage = AutoInvoke.FAST_INPUT_USAGE)\n");
        }
          sb.append("  public "+critere.getType()+" get"+UpperFirst(critere.getNom())+"() {\n");
          sb.append("    return super.get"+critere.getNom()+"();\n");
          sb.append("  }\n");
        }

        // fin getter 
          
        //SETTER
        /*aucune surcharge des setter*/
          
      }
      
      
      return sb.toString();
    }
	  
	  private static String declareProcessInitialize(){
	    StringBuilder sb = new StringBuilder();
	    sb.append("/**\n");
	    sb.append(" * @see sopra.immobilier.service.business.AbstractSQLService#processInitialize(java.sql.Connection, sopra.immobilier.service.business.ServiceContext, sopra.immobilier.service.business.ServiceParameters)\n");
	    sb.append(" */\n");
	    sb.append("@Override\n");
	    sb.append("protected ServiceResponse processInitialize(Connection cnx, ServiceContext context,\n");
	    sb.append("                                            ServiceParameters parameters) throws ServiceException {\n");
	    sb.append("   \n");
	    sb.append("  ServiceResponse response = createDefaultServiceResponse();\n");
	    sb.append("  "+name+"Proxy proxy = new "+name+"Proxy();\n");
	    sb.append("  addElement(response, proxy);\n");
	    sb.append("  return response;\n");
	    sb.append("}\n");
	    return sb.toString();
	  }
	  
	  private static String declareProcessCreate(){
	    StringBuilder sb = new StringBuilder();
	    sb.append("/**\n");
	    sb.append(" * @see sopra.immobilier.service.business.AbstractSQLService#processCreate(java.sql.Connection, sopra.immobilier.service.business.ServiceContext, sopra.immobilier.service.business.ServiceParameters)\n");
	    sb.append(" */\n");
	    sb.append("@Override\n");
	    sb.append("protected ServiceResponse processCreate(Connection cnx,\n");
	    sb.append("                                        ServiceContext context,\n");
	    sb.append("                                       ServiceParameters parameters)\n");
	    sb.append("                                                                   throws ServiceException,\n");
	    sb.append("                                                                   ServiceDuplicateObjectException {\n");
	    sb.append("ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
	    sb.append(""+name+"ServiceParameters serviceParameters =  ("+name+"ServiceParameters) parameters;\n");
	    sb.append("\n");
	    sb.append("//@TODO\n");
	    sb.append(" return serviceResponse;\n");
	    sb.append("}\n");
	  return sb.toString();
    }
	  
	  private static String declareProcessUpdate(){
      StringBuilder sb = new StringBuilder();
      sb.append("/**\n");
      sb.append(" * @see sopra.immobilier.service.business.AbstractSQLService#processUpdate(java.sql.Connection, sopra.immobilier.service.business.ServiceContext, sopra.immobilier.service.business.ServiceParameters)\n");
      sb.append(" */\n");
      sb.append("@Override\n");
      sb.append("protected ServiceResponse processUpdate(Connection cnx,\n");
      sb.append("                                        ServiceContext context,\n");
      sb.append("                                       ServiceParameters parameters)\n");
      sb.append("                                                                   throws ServiceException,\n");
      sb.append("                                                                   ServiceDuplicateObjectException {\n");
      sb.append("ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
      sb.append(""+name+"ServiceParameters serviceParameters =  ("+name+"ServiceParameters) parameters;\n");
      sb.append("\n");
      sb.append("//@TODO\n");
      sb.append(" return serviceResponse;\n");
      sb.append("}\n");
    return sb.toString();
    }
	  
	  private static String declareProcessDelete(){
      StringBuilder sb = new StringBuilder();
      sb.append("/**\n");
      sb.append(" * @see sopra.immobilier.service.business.AbstractSQLService#processDelete(java.sql.Connection, sopra.immobilier.service.business.ServiceContext, sopra.immobilier.service.business.ServiceParameters)\n");
      sb.append(" */\n");
      sb.append("@Override\n");
      sb.append("protected ServiceResponse processDelete(Connection cnx,\n");
      sb.append("                                        ServiceContext context,\n");
      sb.append("                                       ServiceParameters parameters)\n");
      sb.append("                                                                   throws ServiceException,\n");
      sb.append("                                                                   ServiceDuplicateObjectException {\n");
      sb.append("ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
      sb.append(""+name+"ServiceParameters serviceParameters =  ("+name+"ServiceParameters) parameters;\n");
      sb.append("\n");
      sb.append("//@TODO\n");
      sb.append(" return serviceResponse;\n");
      sb.append("}\n");
    return sb.toString();
    }
	  
	  private static String declareMethodeForExportBureautique(){
	    StringBuilder sb = new StringBuilder();
	    sb.append("/**\n");
	    sb.append(" * Retourne un booléen permettant de savoir si le service réalise un export BI.\n");
	    sb.append(" * @return <code>true</code> si le service permet l'export BI, <code>false</code> sinon.\n");
	    sb.append("  * @since Kernel 6.0\n");
	    sb.append("  */\n");
	    sb.append(" @Override\n");
	    sb.append(" public boolean implementsExportBI(){\n");
	    sb.append("   return true;\n");
	    sb.append(" }   \n");
	    sb.append(" \n");
	    sb.append(" /**\n");
	    sb.append("  * @see sopra.immobilier.service.business.AbstractSQLService#processCreate(java.sql.Connection, sopra.immobilier.service.business.ServiceContext, sopra.immobilier.service.business.ServiceParameters)\n");
	    sb.append("  */\n");
	    sb.append(" @Override\n");
	    sb.append(" protected ServiceResponse processCreate(Connection cnx,\n");
	    sb.append("                                         ServiceContext context,\n");
	    sb.append("                                         ServiceParameters parameters)throws ServiceException,ServiceDuplicateObjectException {\n");
	    sb.append("   \n");
	    String code=name.substring(0, name.indexOf("_"));
	    sb.append("   ExportBI"+code+"CreateService service = new ExportBI"+code+"CreateService();\n");
	    sb.append("   return service.processExport(cnx, context, parameters);\n");
	    sb.append(" }  \n");
	    return sb.toString();
	  }
	  
	  private static List<TypeCompute> extractionCompute(List<Proxy> listeProxy){
	    listeCompute = new ArrayList();
	    listeComposantMetier = new ArrayList();
	    for (Proxy proxy : listeProxy) {
	      if (proxy.getTypeCompute()!=null){
	        TypeCompute compute=proxy.getTypeCompute();
	        TypeCompute computePresent=containsCompute(compute.getCode());
	        if (computePresent!=null){
	          String typePresent=computePresent.getType();
	          if (typePresent.isEmpty() && !compute.getType().isEmpty() ){
	            //ajout du type au compute déjá present dans la liste 
	            computePresent.setType(compute.getType());
	          }
	          String servicePresent=computePresent.getService();
            if (servicePresent!=null && servicePresent.isEmpty() && !compute.getService().isEmpty() ){
              //ajout du service au compute déjá present dans la liste 
              computePresent.setService(compute.getService());
            }
            String requetePresent=computePresent.getRequete();
            if (requetePresent.isEmpty() && !compute.getRequete().isEmpty() ){
              //ajout de la requete au compute déjá present dans la liste 
              computePresent.setRequete(compute.getRequete());
            }
            if (compute.getListeParametre()!=null) {
              //ajout de la requete au compute déjá present dans la liste 
              computePresent.setListeParametre(compute.getListeParametre());
            }
            String typeComposantMetier=computePresent.getTypeComposantMetier();
            if (typeComposantMetier!=null && typeComposantMetier.isEmpty() && !compute.getTypeComposantMetier().isEmpty() ){
              //ajout du service au compute déjá present dans la liste 
              computePresent.setTypeComposantMetier(compute.getTypeComposantMetier());
            }
	        }else{
	          TypeCompute computeNouveau=new TypeCompute();
	          computeNouveau.setCode(compute.getCode().trim());
	          if (compute.getType()!=null){
	            computeNouveau.setType(compute.getType().trim());
	            if (compute.getType().equalsIgnoreCase("COM")){
	              if (!listeComposantMetier.contains(compute.getTypeComposantMetier())){
	                listeComposantMetier.add(compute.getTypeComposantMetier());
	              }
	            }
	          }
	          if (compute.getRequete()!=null){computeNouveau.setRequete(compute.getRequete().trim());}
	          if (compute.getService()!=null){computeNouveau.setService(compute.getService().trim());}
	          if (compute.getListeParametre()!=null){computeNouveau.setListeParametre(compute.getListeParametre());}
	          if (compute.getListeParametre()!=null){computeNouveau.setTypeComposantMetier(compute.getTypeComposantMetier());}
	          listeCompute.add(computeNouveau);  
	        }
	      }
	    }
	    return listeCompute; 
	  }
	  
	  private static TypeCompute containsCompute(String code){
	    for (TypeCompute compute : listeCompute) {
	      if (compute.getCode().equalsIgnoreCase(code)){
	        return compute;
	      }
	    }
	    return null;
	  }
	  
	  private static boolean isMulti(){
      for (Criteria criteria : listeCriteria) {
        if (criteria.getMulti()==true){
          return true;
        }
      }
      return false;
    }
	  
	  private static boolean isExport(){
      for (Criteria criteria : listeCriteria) {
        if (criteria.getExport()!=null){
          return true;
        }
      }
      return false;
    }
	  private static TypeExport extractExport(){
      for (Criteria criteria : listeCriteria) {
        if (criteria.getExport()!=null){
          return criteria.getExport();
        }
      }
      return null;
    }
	  
	  private static String UpperFirst(String str){
	    String result=null;
	    String firstLetter=str.substring(0, 1).toUpperCase();
	    result=firstLetter+str.substring(1, str.length());
	    return result;
	  }
}
