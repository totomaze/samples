import java.util.StringTokenizer;


/**
 * 
 * @author geoffrey Thomazeau
 *
 */

public class ParametreEntree {
	private String nom;
	private String description;
	private boolean entree;
	private boolean sortie;
	 private boolean obligatoire ;
	private String type;
	private String typeList;
	
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
  public boolean isObligatoire() {
    return obligatoire;
  }
  public void setObligatoire(boolean obligatoire) {
    this.obligatoire = obligatoire;
  }
  public boolean isEntree() {
    return entree;
  }
  public void setEntree(boolean entree) {
    this.entree = entree;
  }
  public boolean isSortie() {
    return sortie;
  }
  public void setSortie(boolean sortie) {
    this.sortie = sortie;
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
  public ParametreEntree(String ligne) {
		StringTokenizer st = new StringTokenizer(ligne);
		String courante="";
		
			
			System.out.println("Nom Description isEntree isSortie isObligatoire  Type (separation par tabulation)");
			System.out.println("Ligne: -" +ligne+"-");
		
		setNom(st.nextToken("\t").trim());
		setDescription(st.nextToken("\t"));
		
		
		courante=st.nextToken("\t");
		if(courante!=null && courante.equalsIgnoreCase("O")){
      setEntree(true);
    }else if (courante==null){
      setEntree(true);
    }
      
    courante=st.nextToken("\t");
		if(courante!=null && courante.equalsIgnoreCase("O")){
      setSortie(true);
    }else if (courante==null){
      setSortie(true);
    }
		
		courante=st.nextToken("\t");
    if(courante!=null && courante.equalsIgnoreCase("O")){
      setObligatoire(true);
    }
		
		courante=st.nextToken("\t");
		if (courante!=null && (courante.contains("liste") || courante.contains("list")|| courante.contains("List"))){
		  setType("List");
		  int debut=courante.indexOf("<");
		  int fin=courante.indexOf(">");
		  setTypeList(courante.substring(debut+1, fin));
		}else if (courante!=null && ((courante.contains("String")||courante.contains("VC") ))){
		  setType("String");
		  
		}else if (courante!=null && (courante.contains("Integer")||courante.contains("N") )){
		  int debut=courante.indexOf("(");
		  int fin=courante.indexOf(")");
		  if (courante.contains(",") ||courante.contains(".") ){
		    setType("BigDecimal");
		  }else{
		    setType("Integer");
		  }
		}else if (courante!=null && (courante.contains("BigDecimal") || courante.contains("Montant")||courante.contains("N") )){
		  setType("BigDecimal");
		  int debut=courante.indexOf("(");
		  int fin=courante.indexOf(")");
		}else {
		  setType(courante);
		}
	}
	
	
	@Override
	public String toString() {
		return "ParametreEntree [nom=" + nom + ", description=" + description
				+ ", isObligatoire=" + obligatoire + ", isEntree=" + entree + ", isSortie="
				+ sortie + ", type=" + type + "]";
	}
	
	public String declareConstante(){
		StringBuilder sb = new StringBuilder();
		sb.append("/** Paramètre en entrée/sortie : La liste des EntiteCodeLibelleProxy  */\n");
		sb.append("private static final String _ENTITE_"+nom.toUpperCase()+" = \""+nom.toLowerCase()+"\";\n");
		return sb.toString();
	}
	
	public String declareINGetter(){		
   
	  StringBuilder sb = new StringBuilder(" \n /**\n   * @return la valeur positionnée au sein de la propriété : "+ description+"\n   */\n");
		sb.append("public ").append(type);
		 if (type.equalsIgnoreCase("List") && typeList!=null){
	      sb.append("<"+typeList+">");
	    }
		sb.append(" getIn"+nom).append("(){\n");
		sb.append("\t return ");
		if (type.equalsIgnoreCase("List") && typeList!=null){
		  sb.append("("+type);
      sb.append("<"+typeList+">)");
    }else {
      sb.append("("+type+") getInParameters().get(_ENTITE_"+nom.toUpperCase()+");\n");
    }
		sb.append("}\n\n");
		return sb.toString();
	}
	
	public String declareOUTGetter(){   
	  StringBuilder sb = new StringBuilder(" \n /**\n   * @return la valeur positionnée au sein de la propriété : "+ description+"\n   */\n");
    sb.append("public ").append(type);
     if (type.equalsIgnoreCase("List") && typeList!=null){
        sb.append("<"+typeList+">");
      }
    sb.append(" getOut"+nom).append("(){\n");
    sb.append("\t return ");
    if (type.equalsIgnoreCase("List") && typeList!=null){
      sb.append("("+type);
      sb.append("<"+typeList+">)");
    }else {
      sb.append("("+type+") getOutParameters().get(_ENTITE_"+nom.toUpperCase()+");\n");
    }
    sb.append("}\n\n");
    return sb.toString();
  }
	
	public String declareINSetter(){   
    StringBuilder sb = new StringBuilder("  /**\n   * @param ").append(nom).append(" valeur à positionner au sein de la propriété: "+description+"\n   */\n");
    
    sb.append("public void setIn"+nom+"(");
    sb.append(type);
    if (type.equalsIgnoreCase("List") && typeList!=null){
       sb.append("<"+typeList+">");
     }
    sb.append(" _"+nom+"){\n");
    sb.append("  getInParameters().put(_ENTITE_"+nom.toUpperCase()+", _"+nom+");\n");
    sb.append("}\n");
    return sb.toString();
  }
	
	 public String declareOUTSetter(){   
	   StringBuilder sb = new StringBuilder("  /**\n   * @param ").append(nom).append(" valeur à positionner au sein de la propriété: "+description+"\n   */\n");
	    
	    sb.append("public void setOut"+nom+"(");
	    sb.append(type);
	    if (type.equalsIgnoreCase("List") && typeList!=null){
	       sb.append("<"+typeList+">");
	     }
	    sb.append(" _"+nom+"){\n");
	    sb.append("  getOutParameters().put(_ENTITE_"+nom.toUpperCase()+", _"+nom+");\n");
	    sb.append("}\n");
	    return sb.toString();
	  }
	
}
