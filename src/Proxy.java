import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * @author gthomazeau
 *
 */

/**
* Description :
* Donnez une description textuelle du service fourni par cette classe.
*
* <TABLE>
*   <TR>
*     <TD><B>Attributs SVN :</B></TD>
*     <TD>&nbsp;</TD>
*   </TR>
*   <TR>
*     <TD><I>Nom du fichier :</I></TD>
*     <TD>$HeadURL:   O:/Kernel5 Livraison/archives/kernel/tools/eclipse/config/ArchitectureImmobilierCodeTemplates.xml-arc  $</TD>
*   </TR>
*   <TR>
*     <TD><I>Modifié le :</I></TD>
*     <TD>$Date:   Apr 16 2009 11:52:40  $</TD>
*   </TR>
* </TABLE>
*
* @version : $Revision:   1.0  $
* @author  : SOPRA Group - $Author:   tintin  $
*
*/
public class Proxy {
  private String nom;
  private String description;
  private boolean triable;
  private boolean filtrable;
  private boolean lien;
  private String type;
  private String typeList;
  private String longueur;
  private String longueurFraction;//nb apres la virgule
  private boolean sommable=false;//si ce mot cle est pr´sent ce champ sera sommable //facultatif
  private TypeCompute compute=null; //facultatif
  private boolean montant=false; 
	
  /*nom                         triable filtrable lien type [sommable]
    GLGPR_COD Code              O N O String(10) 
    GLGPR_LIB Libellé           O N N String(30)
    GLGPR_DTD Début             O N N Date
    GLGPR_DTF Fin               O N N Date
    GLFAG_COD_MGCUT_LIB Famille O N N String(36)
    NONTANT_COD Famille O N N BigDecimal(8,2) sommable compute(TOTO_LIB;REQ;"select * from toto where toto_cod=?"; toto) SEPARATEUR ;;;;;;;
    compute(TOTO_LIB;SER;toto;TOTO_COD) SEPARATEUR ;;;;;;;
    compute(TOTIE_LIB;COM;TIERS;TOTIE_COD) SEPARATEUR ;;;;;;;
    compute(TOTIE_LIB;COD;)
   */
  
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public boolean isTriable() {
    return triable;
  }
  public void setTriable(boolean triable) {
    this.triable = triable;
  }
  public boolean isFiltrable() {
    return filtrable;
  }
  public void setFiltrable(boolean filtrable) {
    this.filtrable = filtrable;
  }
  public boolean isLien() {
    return lien;
  }
  public void setLien(boolean lien) {
    this.lien = lien;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getTypeList() {
    return typeList;
  }
  public void setTypeList(String typeList) {
    this.typeList = typeList;
  }
  public String getLongueur() {
    return longueur;
  }

  public void setLongueur(String longueur) {
    this.longueur = longueur;
  }

  public String getLongueurFraction() {
    return longueurFraction;
  }

  public void setLongueurFraction(String longueurFraction) {
    this.longueurFraction = longueurFraction;
  }
  public boolean isSommable() {
    return sommable;
  }
  
  public void setSommable(boolean sommable) {
    this.sommable = sommable;
  }
  public TypeCompute getTypeCompute() {
    return compute;
  }
  public void setTypeCompute(TypeCompute typeCompute) {
    this.compute = typeCompute;
  }

  public TypeCompute getCompute() {
    return compute;
  }

  public void setCompute(TypeCompute compute) {
    this.compute = compute;
  }
  public boolean isMontant() {
    return montant;
  }

  public void setMontant(boolean montant) {
    this.montant = montant;
  }
  public Proxy(String ligne) {
    StringTokenizer st = new StringTokenizer(ligne);
    String courante="";

    System.out.println("Nom Description isTriable isFiltrable isLien  isType (separation par tabulation)");
    System.out.println("Ligne: -" +ligne+"-");
    
    setNom(st.nextToken("\t").trim());
    setDescription(st.nextToken("\t"));
    
    
    courante=st.nextToken("\t");
    if(courante!=null && courante.equalsIgnoreCase("O")){
      setTriable(true);
    }else if (courante==null){
      setTriable(true);
    }
      
    courante=st.nextToken("\t");
    if(courante!=null && courante.equalsIgnoreCase("O")){
      setFiltrable(true);
    }else if (courante==null){
      setFiltrable(false);
    }
    
    courante=st.nextToken("\t");
    if(courante!=null && courante.equalsIgnoreCase("O")){
      setLien(true);
    }else if (courante==null){
      setLien(false);
    }
    
    courante=st.nextToken("\t");
    if (courante!=null && (courante.equalsIgnoreCase("liste") || courante.equalsIgnoreCase("list"))){
      setType("List");
      int debut=courante.indexOf("(");
      int fin=courante.indexOf(")");
      setTypeList(courante.substring(debut+1, fin));
    }else if (courante!=null && ((courante.contains("String")||courante.contains("VC") ))){
      setType("String");
      int debut=courante.indexOf("(");
      int fin=courante.indexOf(")");
      if (debut>0){
        setLongueur(courante.substring(debut+1, fin));
      }
    }else if (courante!=null && (courante.contains("Integer")||courante.contains("N") )){
      int debut=courante.indexOf("(");
      int fin=courante.indexOf(")");
      if (courante.contains(",") ||courante.contains(".") ){
        setType("BigDecimal");
        if (debut>0 && courante.contains(",")){    
          int virgule=courante.indexOf(",");
          setLongueur(courante.substring(debut+1, virgule));
          setLongueurFraction(courante.substring(virgule+1, fin));
        }
      }else{
        setType("Integer");
        if (debut>0){
          setLongueur(courante.substring(debut+1, fin));
        }
      }
      
      
    }else if (courante!=null && (courante.contains("BigDecimal") || courante.contains("Montant")||courante.contains("N") )){
      if (courante.contains("Montant")){
        setMontant(true);
        setSommable(true);
      }
      setType("BigDecimal");
      int debut=courante.indexOf("(");
      int fin=courante.indexOf(")");
      if (debut>0 && courante.contains(",")){    
        int virgule=courante.indexOf(",");
        setLongueur(courante.substring(debut+1, virgule));
        setLongueurFraction(courante.substring(virgule+1, fin));
      }
    }else {
      setType(courante);
    }
    int debutParametre=0;
    while (st.hasMoreTokens()){
      courante=st.nextToken("\t"); //complementaire liste
      if(courante!=null){
        if (courante.contains("sommable")){
          setSommable(true) ;
        }
        if (courante.contains("compute")){
          TypeCompute requete=new TypeCompute();
          String[] tableauType=null;
          int debut=courante.indexOf("(");
          int fin=courante.lastIndexOf(")");
          if (debut>0 && courante.contains(";")){    
            tableauType=courante.substring(debut+1, fin).split(";");
            if (tableauType[0]!=null){
              requete.setCode(tableauType[0].trim());//code
            }
            if (tableauType.length>1 && tableauType[1]!=null){
              requete.setType(tableauType[1].trim());//type
              
            }
            if (tableauType.length>2 && tableauType[2]!=null){
              if (requete.getType().equals("REQ")){
                requete.setRequete(tableauType[2].trim());//requete
                debutParametre=3;
              }else if (requete.getType().equals("SER")){
                requete.setService(tableauType[2].trim());//nom du service
                debutParametre=3;
              }else if(requete.getType().equals("COD")){
               debutParametre=2;
              }else if(requete.getType().equals("COM")){
                requete.setTypeComposantMetier(tableauType[2].trim());//nom du service
                debutParametre=3;
              }
              
            }
            // recuperation des paramétres
            List<String> listeparam=new ArrayList<String>();
              for (int i = debutParametre; i < tableauType.length; i++) {
              if (tableauType[i]!=null){
                listeparam.add(tableauType[i]);
              }
            
            }
              requete.setListeParametre(listeparam);
                       
          }else{
            requete.setCode(courante.substring(debut+1, fin));
          }
          setTypeCompute(requete) ;
        }
      }
        
      }
    
	}
	
	
	@Override
	public String toString() {
	  return "ParametreEntree [nom=" + nom + ", description=" + description
    + ", isTriable=" + triable + ", isFiltrable=" + filtrable + ", isLien="
    + lien + ", type=" + type + "]";
	}
	
	public String declareConstante(){
		StringBuilder sb = new StringBuilder("");
		sb.append("/**"+description+"*/ \n");
		sb.append("private ").append(type).append(" ").append("m").append(nom).append(" = null;");
		if (isLien()){
		  sb.append("private ").append("String").append(" ").append("m").append(nom).append("_URL = null;");
		}
		return sb.toString();
	}
	
	public String declareGetterForBaseProxy(){		
	  //StringBuilder sb = new StringBuilder(" \n /**\n   * @return la valeur positionnée au sein de la propriété\n   */\n");
	  StringBuilder sb = new StringBuilder("\n");
		sb.append(declareAnnotation());
		sb.append("public ").append(type).append(" get").append(nom).append("(){\n");
		sb.append("\t return m").append(nom).append(";\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	public String declareSetterForBaseProxy(){		
		//StringBuilder sb = new StringBuilder("  /**\n   * @param ").append(nom).append(" valeur é positionner au sein de la propriété\n   */\n");
	  StringBuilder sb = new StringBuilder("");
		sb.append("public void set").append(nom).append("(").append(type).append(" ").append(nom).append("){\n");
		sb.append("if (isLoggingDebug())\n");
		sb.append("\t logDebug(\" set").append(nom).append("() <-- \" + ").append(nom).append(");\n");
		sb.append("m").append(nom).append(" = ").append(nom).append(";\n");
		sb.append("}");
		return sb.toString();
	}
	
	 public String declareGetterURLProxy(){    
	    //StringBuilder sb = new StringBuilder(" \n /**\n   * @return la valeur positionnée au sein de la propriété\n   */\n");
	   StringBuilder sb = new StringBuilder("");
	    sb.append("public ").append("String").append(" get").append(nom).append("_URL(){\n");
	    sb.append("\t return m").append(nom+"_URL").append(";\n");
	    sb.append("}\n");
	    return sb.toString();
	  }
	  
	  public String declareSetterURLProxy(){    
	    //StringBuilder sb = new StringBuilder("  /**\n   * @param ").append(nom).append(" valeur é positionner au sein de la propriété\n   */\n");
	    StringBuilder sb = new StringBuilder("");
	    sb.append("public void set").append(nom).append("_URL(").append("String").append(" ").append(nom).append("){\n");
	    sb.append("if (isLoggingDebug())\n");
	    sb.append("\t logDebug(\" set").append(nom).append("_URL() <-- \" + ").append(nom).append(");\n");
	    sb.append("m").append(nom).append("_URL = ").append(nom).append(";\n");
	    sb.append("}");
	    return sb.toString();
	  }
	
	public String declareParameters(){
		//StringBuilder sb = new StringBuilder("/** Nom du paramétre donnant accés au ").append(nom).append(" */\n");
		StringBuilder sb = new StringBuilder("");
		sb.append("public static final String ").append(nom).append("_PARAMETER_NAME = \"").append(nom).append("\";\n");
		return sb.toString();
	}
	
	
	public String declareGetterParametersForService(){		
		
		//StringBuilder sb = new StringBuilder(" \n /**\n   * @return la valeur positionnée au sein de la propriété\n   */\n");
	  StringBuilder sb = new StringBuilder("");
		/*Integer max = new Integer(10^length);
    max = max-1;
		if(!nullable){
			sb.append("@RequiredConstraint \n");
		}*/
		sb.append(declareAnnotation());
		sb.append("public ").append(type).append(" get").append(nom).append("(){\n");
		sb.append("\t return getParameterValueAs").append(type).append("(").append(nom).append("_PARAMETER_NAME);\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	
	public String declareCriteria(){
		//StringBuilder sb = new StringBuilder("/** Nom du critère donnant accès au ").append(nom).append(" */\n");
		StringBuilder sb = new StringBuilder("");
		sb.append("public static final String ").append(nom).append("_CRITERION_NAME = \"").append(nom).append("\";\n");
		return sb.toString();
	}
	
	public String declareGetterCriteriaForService(){
		/*int j=1;
		for (int i = 0; i < length; i++) {
			j=j*10;
		}
		Integer max = new Integer(j);
		max = max-1;*/
		//StringBuilder sb = new StringBuilder(" \n /**\n   * @return la valeur du critère \n   */\n");
	  StringBuilder sb = new StringBuilder("");
		/*if(!nullable){
			sb.append("@RequiredConstraint \n");
		}*/
		sb.append(declareAnnotation());
		sb.append("public ").append(type).append(" get").append(nom).append("(){\n");
		sb.append("\t return getCriterionValueAs").append(type).append("(").append(nom).append("_CRITERION_NAME);\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	public String declareAnnotation(){
	  StringBuilder sb =new StringBuilder();
	  if(type.equalsIgnoreCase("Integer")){
	      sb.append("@ForAnInteger(useGrouping=false) \n");
	      //sb.append("@Max(").append(max).append(")\n");
	    }
	    if(type.equalsIgnoreCase("Date")){
	      sb.append("@ForADate \n");
	    }
	    if(type.equalsIgnoreCase("String")){
	      sb.append("@MaxLengthConstraint(").append(longueur).append(")\n");
	    }
	    if(type.equalsIgnoreCase("BigDecimal")){
	      sb.append("@ForADecimal(digits=").append(longueur).append(",fractionDigits=").append(longueurFraction).append(") \n");
	    }
	   return sb.toString();
	}
	
	/*
	public String checkCriterionIsEmpty(){
		StringBuilder sb = new StringBuilder();
		sb.append(" if(serviceCriteria.isCriterionEmpty(").append(tableName).append("BasePrimaryKeyServiceCriteria.").append(name).append("_CRITERION_NAME)||");
		sb.append(" serviceCriteria.hasCriterionWildCard(").append(tableName).append("BasePrimaryKeyServiceCriteria.").append(name).append("_CRITERION_NAME))");
		sb.append("{\n");
		sb.append("	          addError(serviceResponse, new PropertyRequiredServiceError(").append(tableName).append("BasePrimaryKeyServiceCriteria.").append(name).append("_CRITERION_NAME));\n");
		sb.append("	        }\n");
		return sb.toString();
	}*/
	
	/*
	public String checkParameterIsEmpty(){
		StringBuilder sb = new StringBuilder();
		sb.append(" if(serviceParameters.isParameterEmpty(").append(tableName).append("BaseServiceParameters.").append(name).append("_PARAMETER_NAME)){\n");
		sb.append("	          addError(serviceResponse, new PropertyRequiredServiceError(\"Paramétre manquant: \",").append(tableName).append("BaseServiceParameters.").append(name).append("_PARAMETER_NAME));\n");
		sb.append("	        }\n");
		return sb.toString();
	}*/
	
	/*public String insertDataInResultSet(){
		StringBuilder sb = new StringBuilder();
		if(type.equalsIgnoreCase("Integer")){
			sb.append("if(serviceParameters.get").append(nom).append("()!= null)\n");
			sb.append("rs.updateInt(\"").append(nom).append("\", serviceParameters.get").append(nom).append("().intValue());\n");
		}else if(type.equalsIgnoreCase("BigDecimal")){
			sb.append("if(serviceParameters.get").append(nom).append("()!= null)\n");
			sb.append("rs.updateBigDecimal(\"").append(nom).append("\", serviceParameters.get").append(nom).append("());\n");
		}else if(type.equalsIgnoreCase("Date")){
			sb.append("if(serviceParameters.get").append(nom).append("()!= null)\n");
			sb.append("rs.updateDate(\"").append(nom).append("\", DateTools.getInstance().asSQLDate(serviceParameters.get").append(nom).append("()));\n");
		}else if(type.equalsIgnoreCase("String")){
			sb.append("rs.updateString(\"").append(nom).append("\", serviceParameters.get").append(nom).append("());\n");
		}
		
		return sb.toString();
	}*/
	
	public String insertCriteriaStatementForSQLWHEREClause(int index){
		
		StringBuilder sb = new StringBuilder();
		if(type.equalsIgnoreCase("Integer")){
			sb.append("stmt.setInt(").append(index).append(", serviceCriteria.get").append(nom).append("().intValue());\n");
		}else if(type.equalsIgnoreCase("Date")){
			sb.append("stmt.setDate(").append(index).append(", DateTools.getInstance().asSQLDate(serviceCriteria.get").append(nom).append("()));\n");
		}else if(type.equalsIgnoreCase("String")){
			sb.append("stmt.setString(").append(index).append(", serviceCriteria.get").append(nom).append("());\n");
		}else if(type.equalsIgnoreCase("BigDecimal")){
			sb.append("stmt.setBigDecimal(").append(index).append(", serviceCriteria.get").append(nom).append("());\n");
		}
		return sb.toString();
	}

	
	public String insertCriteriaStatementForSQLWHEREClauseForUpdate(int index){
		
		StringBuilder sb = new StringBuilder();
		if(type.equalsIgnoreCase("Integer")){
			sb.append("stmt.setInt(").append(index).append(", serviceParameters.get").append(nom).append("().intValue());\n");
		}else if(type.equalsIgnoreCase("Date")){
			sb.append("stmt.setDate(").append(index).append(", DateTools.getInstance().asSQLDate(serviceParameters.get").append(nom).append("()));\n");
		}else if(type.equalsIgnoreCase("String")){
			sb.append("stmt.setString(").append(index).append(", serviceParameters.get").append(nom).append("());\n");
		}else if(type.equalsIgnoreCase("BigDecimal")){
			sb.append("stmt.setBigDecimal(").append(index).append(", serviceParameters.get").append(nom).append("());\n");
		}
		return sb.toString();
	}
	
	
	
}
