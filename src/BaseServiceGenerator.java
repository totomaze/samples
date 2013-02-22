
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

/**
 * cette classe contient un main qui recherche tous les fichiers <nom_table>.table
 *  qui sont dans le m�me r�pertoire que le .jar et pour chaque fichier cr�e 
 *  un <nom_table>Base.java et <nom_table>BaseService.java. 
 *  Pour chaque morceau de chaque fichier, il y a une m�thode � declare� � qui rajoute ce morceau
 * @author dAlonso
 *
 */

public class BaseServiceGenerator {
	public static String pathforSRC = "D:\\Documents and Settings\\gThomazeau\\Mis documentos\\copyPaste\\BaseServiceGenerator\\src\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
	public static String pathForTable = "D:\\Documents and Settings\\gThomazeau\\Mis documentos\\copyPaste\\BaseServiceGenerator\\src\\";
  public static void main(String[] argv) throws Exception {
	File dir = new File(pathForTable);
    FilenameFilter filterTable = new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return name.endsWith(".table");
        }
      };
    String[] children = dir.list(filterTable);
    
	//Chaque children est un fichier � partir duquel on veut g�n�rer le service de base
    for (String tableNameWithExtension : children) {
    	
		//Getting Table name
    	String tableName = tableNameWithExtension.substring(0,tableNameWithExtension.indexOf(".table"));
		
		//Getting table column, type and mandatory flag
    	ArrayList<DataTypeMandatory> dataList = gettingDataFromFile(pathForTable,tableName,tableNameWithExtension);
    	for (DataTypeMandatory dataTypeMandatory : dataList) {
			System.out.println(dataTypeMandatory);
		}
		
		//Generate BaseClass
    	generateBaseClass(pathforSRC,tableName,dataList);
		
		//Generate BaseService
    	generateServiceClass(pathforSRC,tableName,dataList);
		
	}   
  }
  
	private static void generateServiceClass(String path, 
											 String tableName,
										 	 ArrayList<DataTypeMandatory> dataList) {
		try {
			File file = new File(path+tableName+"BaseService.java");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
			out.append(declarePackage());
			out.append(declareServiceImportServiceClass(dataList));
			out.append(declareServiceClassCommentary(tableName));
			out.append(declareServiceClassDeclaration(tableName));
			out.append(declareServiceClassSQLClause(dataList,tableName));
			out.append(declareServiceNameVersionDescription());
			out.append(declareServiceCriteriaCommentary(tableName));
			out.append(declareServiceCriteriaClass(dataList,tableName));
			out.append(declareServiceParameters(dataList, tableName));
			out.append(declareServiceProcessUpdate(dataList,tableName));
			out.append(declareServiceUpdateTABLENAMEBase(dataList,tableName));
			out.append(declareServiceProcessCreate(dataList,tableName));
			out.append(declareServiceupdateRsFromServiceParameters(dataList,tableName));
			out.append(declareServiceProcessFindByPrimaryKey(dataList,tableName));
			out.append("}//fin classe ").append(tableName).append("BaseService");
			out.close();
		} catch (Exception e) {
			//Exception non soulev�e
		}	
	}

	private static String declareServiceProcessFindByPrimaryKey(
			ArrayList<DataTypeMandatory> dataList, String tableName) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("    /**\n");
		sb.append("     * Permet de rechercher un regroupement de facturation avec sa cl� primaire.\n");
		sb.append("     * @param cnx connexion de lecture au sein de laquelle sera entreprise la \n");
		sb.append("     * recherche\n");
		sb.append("     * @param context contexte permettant d'acc�der au service et de prendre en\n");
		sb.append("     * compte par exemple les droits d'acc�s\n");
		sb.append("     * @param criteria ensemble des valeurs qui permettront de s�lectionner l'objet\n");
		sb.append("     * m�tier (i.e. cl� primaire)\n");
		sb.append("     * @return une r�ponse contenant un et un seul �l�ment correspondant � l'objet\n");
		sb.append("     * m�tier s�lectionn� si aucune erreur fonctionnelle n'a �t� rencontr�e\n");
		sb.append("     * @throws ServiceObjectNotFoundException soulev�e si l'objet recherch�\n");
		sb.append("     * n'existe pas\n");
		sb.append("     * @throws ServiceException soulev�e si une erreur technique ne permet pas\n");
		sb.append("     * d'extraire les informations de la couche de persistance\n");
		sb.append("     */\n");
		sb.append("    @Override\n");
		sb.append("    public ServiceResponse processFindByPrimaryKey(Connection cnx, ServiceContext context,\n");
		sb.append("            ServiceCriteria criteria) \n");
		sb.append("    throws ServiceException, ServiceObjectNotFoundException {\n");
		sb.append("        ServiceResponse serviceResponse = createDefaultServiceResponse();\n");
		sb.append("        //\n");
		sb.append("        // On commence par valider nos crit�res de recherche.\n");
		sb.append("        // le num�ro et la version de contrat sont obligatoires.\n");
		sb.append("        ").append(tableName).append("BasePrimaryKeyServiceCriteria serviceCriteria = new ").append(tableName).append("BasePrimaryKeyServiceCriteria(criteria);\n");
		//On v�rifie la pr�sence des crit�res de la cl� primaire pour la recherche
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			if(dataTypeMandatory.isKey()){
				sb.append(dataTypeMandatory.checkCriterionIsEmpty());
			}
		}
		sb.append("\n");

		sb.append("        if (!serviceResponse.hasErrors()) {\n");
		sb.append("            PreparedStatement stmt = null;\n");
		sb.append("            ResultSet rs = null;\n");
		sb.append("            try {\n");
		sb.append("                StringBuffer requete = new StringBuffer(_SQL_SELECT_CLAUSE);\n");
		sb.append("                requete.append(_SQL_FROM_CLAUSE);\n");
		sb.append("                requete.append(_SQL_PRIMARY_KEY_WHERE_CLAUSE);\n");
		sb.append("                \n");
		sb.append("                stmt  = cnx.prepareStatement(requete.toString()) ;\n");
		//On boucle sur chaque param�tre pour mettre � jour ou cr�er les  objets
		int index =1;
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			if(dataTypeMandatory.isKey()){
				sb.append(dataTypeMandatory.insertCriteriaStatementForSQLWHEREClause(index));
				index= index+1;
			}
		}
		sb.append("                \n");
		sb.append("                rs = stmt.executeQuery();\n");
		sb.append("                if (rs.next()) {\n");
		sb.append("                  ").append(tableName).append("Base glrec = new ").append(tableName).append("Base();\n");
		sb.append("                  glrec = computeBusinessObjectProxyFromResultSet(rs, glrec);\n");
		sb.append("                  serviceResponse = addElement(serviceResponse, glrec);\n");
		sb.append("                } else {\n");
		sb.append("                    ServiceObjectNotFoundException notFoundEx = new ServiceObjectNotFoundException(\"Aucune occurence trouv�e pour les crit�res \");\n");
		sb.append("                    if (isLoggingError())\n");
		sb.append("                        logError(notFoundEx);\n");
		sb.append("                    throw notFoundEx;\n");
		sb.append("                }\n");
		sb.append("                \n");
		sb.append("            } catch (SQLException databaseEx) {\n");
		sb.append("              // Exception soulev�e par l'acc�s � la couche de persistance\n");
		sb.append("              ServiceException ioEx = new ServiceException(\"Erreur technique lors de l'ex�cution de la requ�te en base\",databaseEx);\n");
		sb.append("              if (isLoggingError()) {\n");
		sb.append("                logError(databaseEx);\n");
		sb.append("              }\n");
		sb.append("              throw ioEx;   \n");
		sb.append("            } finally {\n");
		sb.append("              // On ferme l'ensemble des ressources que nous avons ouvertes au sein \n");
		sb.append("              // de la base de donn�es\n");
		sb.append("              if (rs != null) {\n");
		sb.append("                try {\n");
		sb.append("                  rs.close();\n");
		sb.append("                }\n");
		sb.append("                catch (SQLException ignoreEx) {\n");
		sb.append("                  // Exception soulev�e par l'acc�s � la couche de persistance\n");
		sb.append("                  ServiceException ioEx = new ServiceException(\"Erreur technique lors de la lib�ration connexion\",ignoreEx);\n");
		sb.append("                  if (isLoggingError()) {\n");
		sb.append("                    logError(ignoreEx);\n");
		sb.append("                  }\n");
		sb.append("                  throw ioEx; \n");
		sb.append("                }\n");
		sb.append("                rs = null;\n");
		sb.append("              }\n");
		sb.append("              //\n");
		sb.append("              if (stmt != null) {\n");
		sb.append("                try {\n");
		sb.append("                  stmt.close();\n");
		sb.append("                }\n");
		sb.append("                catch (SQLException ignoreEx) {\n");
		sb.append("                  // Exception soulev�e par l'acc�s � la couche de persistance\n");
		sb.append("                  ServiceException ioEx = new ServiceException(\"Erreur technique lors de la lib�ration connexion\",ignoreEx);\n");
		sb.append("                  if (isLoggingError()) {\n");
		sb.append("                    logError(ignoreEx);\n");
		sb.append("                  }\n");
		sb.append("                  throw ioEx; \n");
		sb.append("                }\n");
		sb.append("                stmt = null;\n");
		sb.append("              }\n");
		sb.append("            }      \n");
		sb.append("        } // if (!serviceResponse.hasErrors()) {\n");
		sb.append("        //\n");
		sb.append("        return serviceResponse;\n");
		sb.append("    }\n");
		return sb.toString();
	}

	private static String declareServiceupdateRsFromServiceParameters(
			ArrayList<DataTypeMandatory> dataList, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("    /**\n");
		sb.append("     * @param serviceParameters ensemble des valeurs qui permettront de s�lectionner l'objet\n");
		sb.append("     * @param rs ResultSet � mettre � jour\n");
		sb.append("     * m�tier (i.e. cl� primaire)\n");
		sb.append("     * compte par exemple les droits d'acc�s\n");
		sb.append("     * @throws SQLException soulev�e si erreur de connexion en base\n");
		sb.append("     * @throws ServiceConcurrentUpdateException soulev�e si erreur de connexion en base\n");
		sb.append("     */\n");
		sb.append("    private void updateRsFromServiceParameters(").append(tableName).append("BaseServiceParameters serviceParameters,\n");
		sb.append("                                               ResultSet rs)\n");
		sb.append("                                               throws SQLException,\n");
		sb.append("                                               ServiceConcurrentUpdateException {\n");
		sb.append("        //Mise � jour du ResultSet � partir du serviceParameters\n");
		//On boucle sur chaque param�tre pour mettre � jour ou cr�er les  objets
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
		sb.append("     * Permet de cr�er un nouvel ensemble de donn�es.\n");
		sb.append("     * @param cnx connexion transactionnelle au sein de laquelle la cr�ation devra\n");
		sb.append("     * �tre entreprise\n");
		sb.append("     * @param context contexte permettant d'acc�der au service et de prendre en\n");
		sb.append("     * compte par exemple les droits d'acc�s\n");
		sb.append("     * @param parameters ensemble des valeurs qui permettront de construire un\n");
		sb.append("     * nouvel objet m�tier\n");
		sb.append("     * @return une r�ponse contenant un et un seul �l�ment correspondant � l'objet\n");
		sb.append("     * m�tier cr�� si aucune erreur fonctionnelle n'a �t� rencontr�e\n");
		sb.append("     * @throws ServiceDuplicateObjectException soulev�e si l'op�ration de cr�ation\n");
		sb.append("     * ne peut aboutir car l'objet m�tier existe d�j� (contrainte d'unicit� sur les\n");
		sb.append("     * attributs identifiant l'objet de fa�on non �quivoque)\n");
		sb.append("     * @throws ServiceException soulev�e � chaque appel pour indiquer que la m�thode\n");
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
		//On v�rifie la pr�sence des param�tres de la cl� primaire pour la recherche
		//On d�clare les param�tres
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
		sb.append("     * @param serviceParameters ensemble des valeurs qui permettront de s�lectionner l'objet\n");
		sb.append("     * m�tier (i.e. cl� primaire)\n");
		sb.append("     * @param context contexte permettant d'acc�der au service et de prendre en\n");
		sb.append("     * compte par exemple les droits d'acc�s\n");
		sb.append("     * @param serviceResponse � compl�ter\n");
		sb.append("     * @param modification doit �tre � true si on modifie les donn�es ou � false si on cr�e\n");
		sb.append("     * @throws ServiceObjectNotFoundException soulev�e si l'objet recherch�\n");
		sb.append("     * n'existe pas\n");
		sb.append("     * @throws ServiceException soulev�e si erreur de connexion en base\n");
		sb.append("     * @throws ServiceConcurrentUpdateException soulev�e si erreur de connexion en base\n");
		sb.append("     * @return serviceResponse mis � jour\n");
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
		//On boucle sur chaque param�tre pour mettre � jour ou cr�er les  objets
		int index =1;
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			if(dataTypeMandatory.isKey()){
				sb.append(dataTypeMandatory.insertCriteriaStatementForSQLWHEREClauseForUpdate(index));
				index= index+1;
			}
		}
		sb.append("        rs = stmt.executeQuery(); \n");
		sb.append("        if(modification){\n");
		sb.append("          if (rs.next()) {// On fait la mise � jour\n");
		sb.append("            rs.moveToCurrentRow();\n");
		sb.append("            updateRsFromServiceParameters(serviceParameters, rs);\n");
		sb.append("            \n");
		sb.append("            // On marque ensuite la mise � jour en v�rifiant que l'on ne sait pas fait\n");
		sb.append("            // voler les donn�es par un autre utilisateur\n");
		sb.append("            rs = markRowVersion(rs, context, serviceParameters);\n");
		sb.append("  \n");
		sb.append("            // On transf�re les donn�es modifi�es au sein de la base de donn�es\n");
		sb.append("            rs.updateRow();\n");
		sb.append("            ").append(tableName).append("Base base = new ").append(tableName).append("Base();\n");
		sb.append("            base = computeBusinessObjectProxyFromResultSet(rs, base);\n");
		sb.append("            newServiceResponse = addElement(newServiceResponse, base);                           \n");
		sb.append("     \n");
		sb.append("          } else {  \n");
		sb.append("            // Tentative de mise � jour d'un �l�ment qui n'existe plus au sein\n");
		sb.append("            // de la base de donn�es\n");
		sb.append("            ServiceObjectNotFoundException notFoundEx = new ServiceObjectNotFoundException(\"La demande de mise � jour n'existe pas au sein de la base de donn�es.\"); \n");
		sb.append("            if (isLoggingError())\n");
		sb.append("                logError(notFoundEx);\n");
		sb.append("            throw notFoundEx;\n");
		sb.append("          }\n");
		sb.append("        }else{\n");
		sb.append("          if (rs.next()) {\n");
		sb.append("            // l'ensemble de donn�es existe d�j�, on ne peut �tre en cr�ation\n");
		sb.append("            newServiceResponse = addError(serviceResponse, new ServiceError(\"Regroupement existant d�j�\"));\n");
		sb.append("          }else{\n");
		sb.append("            rs.moveToInsertRow();\n");
		sb.append("            updateRsFromServiceParameters(serviceParameters, rs);\n");
		sb.append("            \n");
		sb.append("            // On marque ensuite la mise � jour en v�rifiant que l'on ne sait pas fait\n");
		sb.append("            // voler les donn�es par un autre utilisateur\n");
		sb.append("            rs = markRowVersion(rs, context, serviceParameters);\n");
		sb.append("            \n");
		sb.append("            // On transf�re les donn�es modifi�es au sein de la base de donn�es\n");
		sb.append("            rs.insertRow();\n");
		sb.append("            ").append(tableName).append("Base base = new ").append(tableName).append("Base();\n");
		sb.append("            base = computeBusinessObjectProxyFromResultSet(rs, base);\n");
		sb.append("            newServiceResponse = addElement(newServiceResponse, base);                                    \n");
		sb.append("          }\n");
		sb.append("        }\n");
		sb.append("      \n");
		sb.append("      }catch (SQLException databaseEx) {\n");
		sb.append("        // Exception soulev�e par l'acc�s � la couche de persistance\n");
		sb.append("        ServiceException ioEx = new ServiceException(\"Erreur technique lors de l'ex�cution de la requ�te en base\",databaseEx);\n");
		sb.append("        if (isLoggingError()) {\n");
		sb.append("          logError(databaseEx);\n");
		sb.append("        }\n");
		sb.append("        throw ioEx;       \n");
		sb.append("      }\n");
		sb.append("      finally {\n");
		sb.append("        // On ferme l'ensemble des ressources que nous avons ouvertes au sein \n");
		sb.append("        // de la base de donn�es\n");
		sb.append("        if (rs != null) {\n");
		sb.append("          try {\n");
		sb.append("            rs.close();\n");
		sb.append("          }\n");
		sb.append("          catch (SQLException ignoreEx) {\n");
		sb.append("            // Exception soulev�e par l'acc�s � la couche de persistance\n");
		sb.append("            ServiceException ioEx = new ServiceException(\"Erreur technique lors de la lib�ration connexion\",ignoreEx);\n");
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
		sb.append("            // Exception soulev�e par l'acc�s � la couche de persistance\n");
		sb.append("            ServiceException ioEx = new ServiceException(\"Erreur technique lors de la lib�ration connexion\",ignoreEx);\n");
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
		sb.append("   * Permet de mettre � jour la table ").append(tableName).append("\n");
		sb.append("   * @param cnx connexion de lecture au sein de laquelle sera entreprise la \n");
		sb.append("   * recherche\n");
		sb.append("   * @param parameters ensemble des valeurs qui permettront de s�lectionner l'objet\n");
		sb.append("   * m�tier (i.e. cl� primaire)\n");
		sb.append("   * @param context contexte permettant d'acc�der au service et de prendre en\n");
		sb.append("   * compte par exemple les droits d'acc�s\n");
		sb.append("   * @return une r�ponse contenant un et un seul �l�ment correspondant � l'objet\n");
		sb.append("   * m�tier s�lectionn� si aucune erreur fonctionnelle n'a �t� rencontr�e\n");
		sb.append("   * @throws ServiceObjectNotFoundException soulev�e si l'objet recherch�\n");
		sb.append("   * n'existe pas\n");
		sb.append("   * @throws ServiceException soulev�e si une erreur technique ne permet pas\n");
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
		
		//On v�rifie la pr�sence des param�tres de la cl� primaire pour la recherche
		//On d�clare les param�tres
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
		sb.append("     * Param�tres de cr�ation, mise � jour ou suppression\n");
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
		sb.append("     *     <TD><I>Modifi� le :</I></TD>\n");
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
		//On d�clare le constructeur
		sb.append("      /**\n");
		sb.append("       * @param originalServiceParameters param�tres � passer permettant la mise � jour ou cr�ation\n");
		sb.append("       */\n");
		sb.append("      public ").append(tableName).append("BaseServiceParameters(ServiceParameters originalServiceParameters) {\n");
		sb.append("        super(originalServiceParameters);\n");
		sb.append("      }\n");
		
		//On d�clare les param�tres
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			sb.append(dataTypeMandatory.declareParameters());
		}
		sb.append("\n");
		
		//On d�clare les getters
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			sb.append(dataTypeMandatory.declareGetterParametersForService());
		}
		
		sb.append("}//fin ").append(tableName).append("BaseServiceParameters\n");
		return sb.toString();
	}

	private static String declareServiceCriteriaClass(
			ArrayList<DataTypeMandatory> dataList, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("    public static final class ").append(tableName).append("BasePrimaryKeyServiceCriteria extends AbstractServiceCriteriaProxy {\n");
		sb.append("        // Constantes\n");
		sb.append("        /** Version extraite du gestionnaire de configuration */\n");
		sb.append("        @SuppressWarnings(\"hiding\") \n");
		sb.append("        public static final String CLASS_VERSION = \"$Revision $\";\n");
		//On boucle sur tous les membres de la cl� primaire
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			if(dataTypeMandatory.isKey()){
				sb.append(dataTypeMandatory.declareCriteria());
			}
		}
		
		//On d�clare le constructeur du service criteria
		sb.append("\n/**\n");
		sb.append("         * Cr�e une nouvelle fa�ade sur les crit�res permettant de s�lection d'un \n");
		sb.append("         * ensemble d'�l�ments m�tier li� � un regroupement de facturation\n");
		sb.append("         * @param criteria crit�res g�n�riques devant �tre masqu� par cette fa�ade\n");
		sb.append("         */\n");
		sb.append("        protected ").append(tableName).append("BasePrimaryKeyServiceCriteria(ServiceCriteria criteria) {\n");
		sb.append("            super(criteria);\n");
		sb.append("        } \n");
		
		//On boucle sur tous les getters des membres de la cl� primaire
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			if(dataTypeMandatory.isKey()){
				sb.append(dataTypeMandatory.declareGetterCriteriaForService());
			}
		}
		
		sb.append("}//fin ").append(tableName).append("BasePrimaryKeyServiceCriteria\n");
		return sb.toString();
	}

	private static String declareServiceCriteriaCommentary(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("    /**\n");
		sb.append("     * Crit�res de recherche de la table ").append(tableName).append("\n");
		sb.append("     *\n");
		sb.append("     * <TABLE>\n");
		sb.append("     *   <TR>\n");
		sb.append("     *     <TD><B>Attributs SVN :</B></TD>\n");
		sb.append("     *     <TD>&nbsp;</TD>\n");
		sb.append("     *   </TR>\n");
		sb.append("     *   <TR>\n");
		sb.append("     *     <TD><I>Nom du fichier :</I></TD>\n");
		sb.append("     *     <TD>$HeadURL: http://svn.lyon.fr.sopra/svnulis/svn/ulis_test/repository/Refonte/ULISNG/REFERENTIEL/trunk/src/main/java/sopra/immobilier/business/ULIS/referentiel/").append(tableName).append("BaseService.java $</TD>\n");
		sb.append("     *   </TR>\n");
		sb.append("     *   <TR>\n");
		sb.append("     *     <TD><I>Modifi� le :</I></TD>\n");
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
		sb.append("   * Retourne le nom du service m�tier\n");
		sb.append("   * @return la valeur positionn�e au sein de la propri�t�\n");
		sb.append("   */\n");
		sb.append("  public String getName() {\n");
		sb.append("    return SERVICE_NAME;\n");
		sb.append("  }\n");
		sb.append("  /**\n");
		sb.append("   * Retourne une description textuelle du service m�tier\n");
		sb.append("   * @return la valeur positionn�e au sein de la propri�t�\n");
		sb.append("   */\n");
		sb.append("  public String getDescription() {\n");
		sb.append("    return SERVICE_DESCRIPTION;\n");
		sb.append("  }\n");
		sb.append("  /**\n");
		sb.append("   * Retourne la version du service m�tier sous forme de cha�ne de caract�res\n");
		sb.append("   * @return la valeur positionn�e au sein de la propri�t�\n");
		sb.append("   */\n");
		sb.append("  public String getVersion() {\n");
		sb.append("    return CLASS_VERSION;\n");
		sb.append("  }\n\n");
		return sb.toString();
	}

	private static String declareServiceClassSQLClause(ArrayList<DataTypeMandatory> dataList, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("  /** Clause SELECT d'acc�s � la table */\n");
		sb.append("  private static final String _SQL_SELECT_CLAUSE = \"SELECT \"+\n");
		//on rajoute toutes les colonnes n�cessaires
		sb.append("                                                  \" ");
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			sb.append(dataTypeMandatory.getName()).append(", ");
		}
		sb.append("\" +\n");
		//On d�clare les variables de compostage
		sb.append("                                                  \" U_VERSION as \" + AbstractULISSQLService.NUM_VERSION_COLUMN_NAME + \",\" +\n");
		sb.append("                                                  \" GLREC_UTICRE as \" + AbstractULISSQLService.CREATION_MGUTI_COD_COLUMN_NAME + \",\" +\n");
		sb.append("                                                  \" GLREC_UTIMAJ as \" + AbstractULISSQLService.MODIFICATION_MGUTI_COD_COLUMN_NAME+\",\" +\n");
		sb.append("                                                  \" GLREC_DATCRE as \" + AbstractULISSQLService.CREATION_DATE_COLUMN_NAME + \",\" +\n");
		sb.append("                                                  \" GLREC_DATMAJ as \" + AbstractULISSQLService.MODIFICATION_DATE_COLUMN_NAME;\n");
		sb.append("  /** Clause FROM d'acc�s � la table */\n");
		sb.append("  private static final String _SQL_FROM_CLAUSE = \" FROM ").append(tableName).append("\";\n");
		sb.append("  /** Clause WHERE d'acc�s � la cl� primaire de la table */\n");
		sb.append("  private static final String _SQL_PRIMARY_KEY_WHERE_CLAUSE = \" WHERE ");
		int i =0;
		for (DataTypeMandatory dataTypeMandatory : dataList) {
			if(dataTypeMandatory.isKey()){
				if(i==0){
					sb.append(tableName).append(".").append(dataTypeMandatory.getName()).append(" = ? \"");
				}else{
					sb.append("+\n \" and ").append(tableName).append(".").append(dataTypeMandatory.getName()).append(" = ? \"");
				}
				i=i+1;
			}
		}
		sb.append(";\n");
		return sb.toString();
	}

	private static String declareServiceClassCommentary(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("/**\n");
		sb.append(" * Description :\n");
		sb.append(" * Service de base d'acc�s � la table ").append(tableName).append("\n");
		sb.append(" *\n");
		sb.append(" * <TABLE>\n");
		sb.append(" *   <TR>\n");
		sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
		sb.append(" *     <TD>&nbsp;</TD>\n");
		sb.append(" *   </TR>\n");
		sb.append(" *   <TR>\n");
		sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
		sb.append(" *     <TD>$HeadURL: http://svn.lyon.fr.sopra/svnulis/svn/ulis_test/repository/Refonte/ULISNG/REFERENTIEL/trunk/src/main/java/sopra/immobilier/business/ULIS/referentiel/").append(tableName).append("BaseService.java $</TD>\n");
		sb.append(" *   </TR>\n");
		sb.append(" *   <TR>\n");
		sb.append(" *     <TD><I>Modifi� le :</I></TD>\n");
		sb.append(" *     <TD>$Date: 2010-10-18 18:14:44 +0200 (lun, 18 oct 2010) $</TD>\n");
		sb.append(" *   </TR>\n");
		sb.append(" * </TABLE>\n");
		sb.append(" *\n");
		sb.append(" * @version : $Revision: 18332 $\n");
		sb.append(" * @author  : SOPRA Group - $Author: dalonso $\n");
		sb.append(" *\n");
		sb.append(" */\n");
		return sb.toString();
	}

	private static String declareServiceClassDeclaration(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("public class ").append(tableName).append("BaseService extends AbstractULISSQLService {\n");
		sb.append("  //Constantes\n");
		sb.append("  /** Version extraite du gestionnaire de configuration */\n");
		sb.append("  @SuppressWarnings(\"hiding\")\n");
		sb.append("  public static final String CLASS_VERSION = \"$Revision: $\";\n");
		sb.append("  //Attributs\n");
		sb.append("		  /** Nom logique du service */\n");
		sb.append("		  public static final String SERVICE_NAME = \"ULIS.referentiel.").append(tableName).append("BaseService\";\n");
		sb.append("		  /** Description textuelle du service */\n");
		sb.append("		  public static final String SERVICE_DESCRIPTION = \"Service de base de la table ").append(tableName).append("\";\n");
		return sb.toString();
	}

	private static String declareServiceImportServiceClass(ArrayList<DataTypeMandatory> dataList) {
		StringBuilder sb = new StringBuilder();		
		sb.append("import java.sql.Connection;\n");
		sb.append("import java.sql.PreparedStatement;\n");
		sb.append("import java.sql.ResultSet;\n");
		sb.append("import java.sql.SQLException;\n");
		sb.append("import sopra.core.util.DateTools;\n");
		sb.append("import sopra.core.annotation.constraints.MaxLengthConstraint;\n");
		sb.append("import sopra.core.annotation.constraints.RequiredConstraint;\n");
		sb.append("import sopra.immobilier.business.ULIS.AbstractULISSQLService;\n");
		sb.append("import sopra.immobilier.service.business.AbstractServiceCriteriaProxy;\n");
		sb.append("import sopra.immobilier.service.business.AbstractServiceParametersProxy;\n");
		sb.append("import sopra.immobilier.service.business.PropertyRequiredServiceError;\n");
		sb.append("import sopra.immobilier.service.business.ServiceConcurrentUpdateException;\n");
		sb.append("import sopra.immobilier.service.business.ServiceContext;\n");
		sb.append("import sopra.immobilier.service.business.ServiceCriteria;\n");
		sb.append("import sopra.immobilier.service.business.ServiceDuplicateObjectException;\n");
		sb.append("import sopra.immobilier.service.business.ServiceError;\n");
		sb.append("import sopra.immobilier.service.business.ServiceException;\n");
		sb.append("import sopra.immobilier.service.business.ServiceObjectNotFoundException;\n");
		sb.append("import sopra.immobilier.service.business.ServiceParameters;\n");
		sb.append("import sopra.immobilier.service.business.ServiceResponse;\n");
		if(contains(dataList, "BigDecimal")){
			sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForADecimal;\n");
			sb.append("import java.math.BigDecimal;\n");
		}
		if(contains(dataList, "Integer")){
			sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForAnInteger;\n");
			sb.append("import javax.validation.constraints.Max;\n");
		}
		if(contains(dataList, "Date")){
			sb.append("import sopra.immobilier.service.business.annotation.descriptors.ForADate;\n");
			sb.append("import java.util.Date;\n");
		}
		return sb.toString();
	}

	private static ArrayList<DataTypeMandatory> gettingDataFromFile(String path, String tableName,String tableNameWithExtension) {
		ArrayList<DataTypeMandatory> dataList = new ArrayList<DataTypeMandatory>();
		try { 
			BufferedReader in = new BufferedReader(new FileReader(path+tableNameWithExtension)); 
			String str; 
			
			while ((str = in.readLine()) != null) { 
				DataTypeMandatory data = new DataTypeMandatory(tableName,str);
				dataList.add(data);
			} 
			in.close(); 
			
		} catch (FileNotFoundException e) {e.printStackTrace(); } 
		catch (IOException ioe){}
		return dataList;	    
	}

	private static void generateBaseClass(String path, String tableName, ArrayList<DataTypeMandatory> dataList){
		
		try {
			File file = new File(path+tableName+"Base.java");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
			out.append(declarePackage());
			out.append(declareImportBaseClass(dataList));
			out.append(declareProxyClassCommentary(tableName));
			out.append(declareClasseDeclaration(tableName));
			for (DataTypeMandatory dataTypeMandatory : dataList) {
				out.append(dataTypeMandatory.declareConstante());
				out.append("\n");				
			}
			for (DataTypeMandatory dataTypeMandatory : dataList) {
				out.append(dataTypeMandatory.declareGetterForBaseProxy());
				out.append(dataTypeMandatory.declareSetterForBaseProxy());
				out.append("\n");				
			}
			out.append("}//fin classe ").append(tableName).append("Base");
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

	private static String declareProxyClassCommentary(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n/**\n");
		sb.append(" * Repr�sente un proxy de base de la table ").append(tableName).append("\n");
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
		sb.append(" *     <TD><I>Modifi� le :</I></TD>\n");
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

	private static String declareImportBaseClass(ArrayList<DataTypeMandatory> dataList) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nimport sopra.immobilier.business.ULIS.AbstractBusinessObject;\n");
		if(contains(dataList,"BigDecimal"))
			sb.append("import java.math.BigDecimal;\n");
		if(contains(dataList,"Date"))
			sb.append("import java.util.Date;\n");
		System.out.println("Don't forget to open the result file on eclipse and check import.");
		return sb.toString();		
	}

	private static String declarePackage() {
		StringBuilder sb = new StringBuilder();
		sb.append("/**\n").append("* Paquet de d�finition\n").append("*/\n");
		sb.append("package sopra.immobilier.business.ULIS.referentiel; \n\n");
		return sb.toString();
	}
	
	private static boolean contains(ArrayList<DataTypeMandatory> dataList, String type){
		boolean found = false;
		Iterator<DataTypeMandatory> it = dataList.iterator();
		while (it.hasNext()&&(!found)) {
			DataTypeMandatory dataTypeMandatory = it.next();
			found = dataTypeMandatory.getType().equalsIgnoreCase(type);
		}
		return found;
		
	}

  
}
