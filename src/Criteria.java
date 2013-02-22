import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * @author gthomazeau
 * Nom Libelle Obligatoire(O/N) Modification(O/N) Type     ListeDeroulante(typede liste,affichage,nom regle/nom codif/nom table,[nom champ code]) [extra type : liste bloc panier...]
 * xx  xx      O                O                 String()  liste(COD_LIB,SPE,GLTAX)  //liste avec un service speciale (SPE)
 * xx  xx      O                O                 String  liste(COD_LIB,COD,COD_LIB,GLTAM) // liste de type codification
 * xx  xx      O                O                 String  liste(COD_LIB,TAB,GLTAM,GLTAM_COD,[GLTAM_ETA]) // liste de type table
 *                                                        liste(COD_LIB,MAN,{A:Aucun,T:libellé Tiers},empty)
 * xx  xx      O                O                 List<XX>  panier(generique,nomChampCod,nomChampLib,nomTable,[nomChampEtat, ETAT, clause])
 * xx  xx      O                O                 List<XX>  panier(fonction,nomFunction)
 * xx  xx      O                O                 String  paesi() bloc(autre,autre recherche)
 * xx  xx      O                O                 String  toeso() bloc(autre)
 * xx  xx      O                O                 String  glcon() bloc(autre)
 * xx  xx      O                O                 BigDecimal(8,2) bloc(autre)
 * xx  xx      O                O                 Integer(O//Separateur de milier) bloc(autre)
 * xx  xx      O                O                 boolean(O//cochee par defaut) bloc(autre)
 *                                                fast(GLRCL001)
 * extratype : select(XXX) from (XXX) where(XXX) export multi
 * controle(TA012,erreur lors du chargement)
 */

public class Criteria {
  private String nom;
  private String description;
  private boolean obligatoire;
  private boolean modification;
  private String type;
  private TypeListeDeroulante listeDeroulante;
  private Integer longueur;
  private Integer longueurFraction;//nb apres la virgule
  private TypePanier panier;
  private TypeComposantMetier composantMetier;
  private TypeFastInput fastInput;
  private TypeBloc bloc;
  private boolean cocheeParDefaut=false;//uniquement pour type boolean
  private boolean dateInitialisee=false;//uniquement pour type date
  private String select=null;//facultatif (select du where principal)
  private String from=null;//facultatif (from du where principal)
  private String where=null;//facultatif (where du where principal)
  private boolean multi=false;//facultatif indique que le resultat est de type multi occurence
  private TypeExport export=null;//facultatif indique le service doit faire appel à l'export bureautique export(codeTableExtraction)
  private TypeControle controle=null;//facultatif indique le service doit controller dans le onValueChange
  private String property=null;//permet de faire un regroupement des champs par property

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
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  public TypeListeDeroulante getListeDeroulante() {
    return listeDeroulante;
  }
  public void setListeDeroulante(TypeListeDeroulante listeDeroulante) {
    this.listeDeroulante = listeDeroulante;
  }

  public Integer getLongueur() {
    return longueur;
  }
  public void setLongueur(Integer longueur) {
    this.longueur = longueur;
  }
  public boolean isObligatoire() {
    return obligatoire;
  }
  public void setObligatoire(boolean obligatoire) {
    this.obligatoire = obligatoire;
  }
  public boolean isModification() {
    return modification;
  }
  public void setModification(boolean modification) {
    this.modification = modification;
  }
  public Integer getLongueurFraction() {
    return longueurFraction;
  }
  public void setLongueurFraction(Integer longueurFraction) {
    this.longueurFraction = longueurFraction;
  }
  public TypePanier getPanier() {
    return panier;
  }
  public void setPanier(TypePanier panier) {
    this.panier = panier;
  }
  public TypeComposantMetier getComposantMetier() {
    return composantMetier;
  }
  public void setComposantMetier(TypeComposantMetier composantMetier) {
    this.composantMetier = composantMetier;
  }
  public TypeFastInput getFastInput() {
    return fastInput;
  }
  public void setFastInput(TypeFastInput fastInput) {
    this.fastInput = fastInput;
  }
  public TypeBloc getBloc() {
    return bloc;
  }
  public void setBloc(TypeBloc bloc) {
    this.bloc = bloc;
  }

  public boolean isCocheeParDefaut() {
    return cocheeParDefaut;
  }

  public void setCocheeParDefaut(boolean cocheeParDefaut) {
    this.cocheeParDefaut = cocheeParDefaut;
  }

  public boolean isDateInitialisee() {
    return dateInitialisee;
  }

  public void setDateInitialisee(boolean dateInitialisee) {
    this.dateInitialisee = dateInitialisee;
  }

  public String getSelect() {
    return select;
  }

  public void setSelect(String select) {
    this.select = select;
  }
  public String getFrom() {
    return from;
  }
  public void setFrom(String from) {
    this.from = from;
  }
  public String getWhere() {
    return where;
  }
  public boolean getMulti() {
    return multi;
  }
  public void setMulti(boolean multi) {
    this.multi = multi;
  }
  public void setWhere(String where) {
    this.where = where;
  }
  public TypeExport getExport() {
    return export;
  }
  public void setExport(TypeExport export) {
    this.export = export;
  }

  public TypeControle getControle() {
    return controle;
  }
  public void setControle(TypeControle controle) {
    this.controle = controle;
  }
  public String getProperty() {
    return property;
  }
  public void setProperty(String property) {
    this.property = property;
  }
  
  public Criteria(String ligne) {
    StringTokenizer st = new StringTokenizer(ligne);
    String courante="";

    System.out.println("Nom Description Obligatoire Modification Type Extra(Liste())  (separation par tabulation)");
    System.out.println("Ligne: -" +ligne+"-");
    
    setNom(st.nextToken("\t").trim());
    setDescription(st.nextToken("\t"));
    
    
    courante=st.nextToken("\t");
    if(courante!=null && courante.equalsIgnoreCase("O")){
      setObligatoire(true);
    }else if (courante==null){
      setObligatoire(false);
    }
      
    courante=st.nextToken("\t");
    if(courante!=null && courante.equalsIgnoreCase("O")){
      setModification(true);
    }else if (courante==null){
      setModification(true);
    }
    
    courante=st.nextToken("\t");
    if (courante!=null && (courante.contains("String")||courante.contains("VC") )){
      setType("String");
      int debut=courante.indexOf("(");
      int fin=courante.indexOf(")");
      if (debut>0){
        setLongueur(Integer.parseInt(courante.substring(debut+1, fin)));
      }
    }else if (courante!=null && (courante.contains("Integer")||courante.contains("N") )){
      int debut=courante.indexOf("(");
      int fin=courante.indexOf(")");
      if (courante.contains(",") ||courante.contains(".") ){
        setType("BigDecimal");
        if (debut>0 && courante.contains(",")){    
          int virgule=courante.indexOf(",");
          setLongueur(Integer.parseInt(courante.substring(debut+1, virgule)));
          setLongueurFraction(Integer.parseInt(courante.substring(virgule+1, fin)));
        }
      }else{
        setType("Integer");
        if (debut>0){
          setLongueur(Integer.parseInt(courante.substring(debut+1, fin)));
        }
      }
    }else if (courante!=null && (courante.contains("BigDecimal") ||courante.contains("Montant")|| courante.contains("N"))){
      setType("BigDecimal");
      int debut=courante.indexOf("(");
      int fin=courante.indexOf(")");
      if (debut>0 && courante.contains(",")){ 
        int virgule=courante.indexOf(",");
        setLongueur(Integer.parseInt(courante.substring(debut+1, virgule)));
        setLongueurFraction(Integer.parseInt(courante.substring(virgule+1, fin)));
      }
    }else if (courante!=null && (courante.contains("Boolean") )){
      setType("Boolean");
      int debut=courante.indexOf("(");
      int fin=courante.indexOf(")");
      if (debut>0){
        String bool=courante.substring(debut+1, fin);
        if (bool.equalsIgnoreCase("O")){
          setCocheeParDefaut(true);
        }
      }
    }else if (courante!=null && (courante.contains("Date") )){
      setType("Date");
      int debut=courante.indexOf("(");
      int fin=courante.indexOf(")");
      if (debut>0){
        String bool=courante.substring(debut+1, fin);
        if (bool.equalsIgnoreCase("O")){
          setDateInitialisee(true);
        }
      }
    }else {
      setType(courante);
    }
    while (st.hasMoreTokens()){
      courante=st.nextToken("\t"); //complementaire extra type
      if(courante!=null){
        if (courante.contains("liste")){
          setListeDeroulante(traitementListe(courante)) ;
        }else if (courante.contains("select")){
          int debut=courante.indexOf("(");
          int fin=courante.lastIndexOf(")");
          setSelect(courante.substring(debut+1, fin));
        }else if (courante.contains("from")){
          int debut=courante.indexOf("(");
          int fin=courante.lastIndexOf(")");
          setFrom(courante.substring(debut+1, fin));
        }else if (courante.contains("where")){
          int debut=courante.indexOf("(");
          int fin=courante.lastIndexOf(")");
          setWhere(courante.substring(debut+1, fin));
        }else if (courante.contains("panier")){
          setPanier(traitementPanier(courante));
        }else if (courante.contains("paesi") || courante.contains("toeso") || courante.contains("totie") || courante.contains("glcon")||courante.contains("caint")
          ||courante.contains("Ref")||courante.contains("observation")){
          setComposantMetier(traitementComposantMetier(courante));         
        }else if (courante.contains("fast")){
          setFastInput(traitementFastInput(courante));
        }else if (courante.contains("bloc")){
        setBloc(traitementBloc(courante));
        }else if (courante.contains("multi")){
          setMulti(true);
        }else if (courante.contains("export")){
          setExport(traitementExport(courante));
      }else if (courante.contains("controle")){
        TypeControle tc=new TypeControle();
        int debut=courante.indexOf("(");
        int fin=courante.lastIndexOf(")");
        if (debut>0 && courante.contains(",")){
          int virgule=courante.indexOf(",");
          tc.setCodeErreur(courante.substring(debut+1, virgule));
          tc.setLibelleErreur(courante.substring(virgule+1, fin));
          
        }
          setControle(tc);
      }else if (courante.contains("property")){
        int debut=courante.indexOf("(");
        int fin=courante.lastIndexOf(")");
        if (debut>0){
          setProperty(courante.substring(debut+1, fin));
        }
      }
        
        
      }
    }
  }
  
  private TypeComposantMetier traitementComposantMetier(String type){
    TypeComposantMetier composant = new TypeComposantMetier();
    int debut=type.indexOf("(");
    int fin=type.indexOf(")");
    if (fin>debut ){    
      //on recupere le type qui se trouve avant les parenthese
      composant.setType(type.substring(0, fin-1));
    //@TODO traiter l attribut nomCriterionName
    }else{
      composant.setType(type);
    }
    return composant;
  }
  
  private TypeFastInput traitementFastInput(String type){
    TypeFastInput fastInput = new TypeFastInput();
    int debut=type.indexOf("(");
    int fin=type.indexOf(")");
    if (fin>debut ){    
      fastInput.setNomFonction(type.substring(debut+1, fin));
    }
    return fastInput;
  }
  
  private TypeBloc traitementBloc(String type){
    TypeBloc bloc = new TypeBloc();
    String[] tableauType=null;
    int debut=type.indexOf("(");
    int fin=type.indexOf(")");
    if (fin>debut && type.contains(",")){    
      tableauType=type.substring(debut+1, fin).split(",");
      if (tableauType[0]!=null){
        bloc.setCode(tableauType[0].trim());//code
      }
      if (tableauType.length>1 && tableauType[1]!=null){
        bloc.setLibelle(tableauType[1].trim());//libellé
      }
      if (tableauType.length>2 && tableauType[2]!=null){
        if (tableauType[2].trim().equalsIgnoreCase("O") || tableauType[2].trim().equalsIgnoreCase("Y")){
          bloc.setReduitParDefaut(true);//reduit par defaut
        }
      }
      if (tableauType.length>3 && tableauType[3]!=null){
        bloc.setLogicalName(tableauType[3].trim());//libellé
      }
      if (tableauType.length>4 && tableauType[4]!=null){
        bloc.setNbChampParLigne(Integer.parseInt(tableauType[4].trim()));//libellé
      }
    }else{
      bloc.setCode(type.substring(debut+1, fin));//code
    }
    return bloc;
  }
  
  private TypeListeDeroulante traitementListe (String type){
    String[] tableauTypeListe=null;
    int debut=type.indexOf("(");
    int fin=type.indexOf(")");
    if (fin>debut && type.contains(",")){    
      tableauTypeListe=type.substring(debut+1, fin).split(",");
    }
    TypeListeDeroulante typeListeDeroulante = new TypeListeDeroulante();
    if (tableauTypeListe[0]!=null){
      typeListeDeroulante.setTypeAffichage(tableauTypeListe[0]);
    }
    if (tableauTypeListe[1]!=null && tableauTypeListe[1].equalsIgnoreCase("SPE")){
      typeListeDeroulante.setType(tableauTypeListe[1]);
      if (tableauTypeListe.length>2 && tableauTypeListe[2]!=null){
        typeListeDeroulante.setNomRegle(tableauTypeListe[2]);
      }else{
        System.out.println("le type de liste deroulante este de la forme suivante List(COD_LIB,SPE,NOM REGLE)");
      }
    }
    if (tableauTypeListe[1]!=null && tableauTypeListe[1].equalsIgnoreCase("COD")){
      typeListeDeroulante.setType(tableauTypeListe[1]);
      if (tableauTypeListe.length>2 && tableauTypeListe[2]!=null){
        typeListeDeroulante.setNomCodif(tableauTypeListe[2]);
      }else{
        System.out.println("le type de liste deroulante este de la forme suivante List(COD_LIB,COD,NOM CODIF)");
      }
    }
    if (tableauTypeListe[1]!=null && tableauTypeListe[1].equalsIgnoreCase("TAB")){
      typeListeDeroulante.setType(tableauTypeListe[1]);
      if (tableauTypeListe.length>2 && tableauTypeListe[2]!=null){
        typeListeDeroulante.setNomTable(tableauTypeListe[2]);
      }else{
        System.out.println("le type de liste deroulante este de la forme suivante List(COD_LIB,TAB,NomTable,NOM CHAMP CODE,nom CHamp_lib,[NOM CHAMP ETAT])");
      }
      if (tableauTypeListe.length>3 && tableauTypeListe[3]!=null && !tableauTypeListe[3].equals("")){
        typeListeDeroulante.setNomChampCod(tableauTypeListe[3]);
      }else{
        System.out.println("le type de liste deroulante este de la forme suivante List(COD_LIB,TAB,NomTable,NOM CHAMP CODE,nom CHamp_lib,[NOM CHAMP ETAT])");
      }
      if (tableauTypeListe.length>4 &&tableauTypeListe[4]!=null && !tableauTypeListe[4].equals("")){
        typeListeDeroulante.setNomChampLib(tableauTypeListe[4]);
      }else{
        System.out.println("le type de liste deroulante este de la forme suivante List(COD_LIB,TAB,NomTable,NOM CHAMP CODE,nom CHamp_lib,[NOM CHAMP ETAT])");
      }
      if (tableauTypeListe.length>5 &&tableauTypeListe[5]!=null && !tableauTypeListe[5].equals("")){
        typeListeDeroulante.setNomChampEtat(tableauTypeListe[5]);
      }else{
        System.out.println("le type de liste deroulante este de la forme suivante List(COD_LIB,TAB,NomTable,NOM CHAMP CODE,nom CHamp_lib,[NOM CHAMP ETAT])");
      }
    }
    if (tableauTypeListe[1]!=null && tableauTypeListe[1].equalsIgnoreCase("MAN")){
      typeListeDeroulante.setType(tableauTypeListe[1]);
      if (tableauTypeListe.length>2 && tableauTypeListe[2]!=null){
        //traitement des paramètres
        Map<String, String> mapParam=new HashMap<String, String>();
        int debutParam=type.indexOf("{");
        int finParam=type.indexOf("}");
        String[] tableauParam=null;
          if (finParam>debutParam && type.contains(",")){    
            tableauParam=type.substring(debutParam+1, finParam).split(",");
          }else{
            tableauParam= new String[1];
            tableauParam[0]=type.substring(debutParam+1, finParam);
          }
          for(String coupleParam:tableauParam){
            if (coupleParam!=null && !"".equals(coupleParam)){
              String[] param=coupleParam.split(":");
              mapParam.put(param[0], param[1]);
              
            }
          }
        typeListeDeroulante.setListeOption(mapParam);
        //traitement des extra type
        if (tableauTypeListe.length>2 && tableauTypeListe[2]!=null){
          for (int i = 3; i < tableauParam.length; i++) {
            if ("empty".equalsIgnoreCase(tableauParam[i])){
              typeListeDeroulante.setEmptyChoice(true);
            }
            
          }
        }
      }else{
        System.out.println("le type de liste deroulante este de la forme suivante List(COD_LIB,MAN,{A:aucun,T:tous}[,empty])  les accolades sont obligatoires");
      }

    }
    return typeListeDeroulante;
    
  }
  
   private TypePanier traitementPanier (String type){
      String[] tableauTypePanier=null;
      int debut=type.indexOf("(");
      int fin=type.indexOf(")");
      if (fin>debut && type.contains(",")){    
        tableauTypePanier=type.substring(debut+1, fin).split(",");
      }
      TypePanier typePanier = new TypePanier();
      if (tableauTypePanier[0]!=null){
        typePanier.setType(tableauTypePanier[0]);
      }
      if (typePanier.getType()!=null){
        //type panier GENERIQUE 
        if (typePanier.getType().equalsIgnoreCase("generique")){
          boolean error=false;
          if (tableauTypePanier[1]!=null){
            typePanier.setNomChampCod(tableauTypePanier[1]);
          }else{error=true;}
          if (tableauTypePanier[2]!=null){
            typePanier.setNomChampLib(tableauTypePanier[2]);
          }else{error=true;}
          if (tableauTypePanier[3]!=null){
            typePanier.setNomTable(tableauTypePanier[3]);
          }else{error=true;}
          
          //Traitement des champs facultatif
          if (tableauTypePanier.length>4){
              typePanier.setNomChampEtat(tableauTypePanier[4]);
              if (tableauTypePanier.length>5){
                typePanier.setETAT(tableauTypePanier[5]);
                if (tableauTypePanier.length>6){
                  typePanier.setClause(tableauTypePanier[6]);
                }
              }
          }
          
          if (!error){
            System.out.println("le type de liste deroulante este de la forme suivante panier(generique,nomChampCod,nomChampLib,nomTable,[nomChampEtat, ETAT, clause]) les champs entre [] sont facultatif");
          }
        //type panier FONCTION  
        }else if(typePanier.getType().equalsIgnoreCase("fonction")){
          boolean error=false;
          if (tableauTypePanier[1]!=null){
            typePanier.setNomFonction(tableauTypePanier[1]);
          }else{error=true;}
                    
          if (!error){
            System.out.println("le type de liste deroulante este de la forme suivante panier(fonction,nomFunction) ");
          }
        }
      }
      
      
      return typePanier;
      
    }
   
   private TypeExport traitementExport (String type){
     TypeExport exp=new TypeExport();
     List<String> listeParam=null;
     String[] tableau=null;
     int debut=type.indexOf("(");
     int fin=type.indexOf(")");
     if (fin>debut && type.contains(",")){    
       tableau=type.substring(debut+1, fin).split(",");
       if (tableau!=null && tableau.length>=1){
         exp.setCodeTable(tableau[0]);
       }
       if (tableau!=null && tableau.length>=2){
         listeParam=new ArrayList<String>();
         for (int i = 1; i < tableau.length; i++) {
           listeParam.add(tableau[i]);
        }
         exp.setCodeTable(tableau[0]);
         exp.setListeChampExport(listeParam);
       }
     }else{
       exp.setCodeTable(type.substring(debut+1, fin));
     }
     return exp;
   }
  
  @Override
  public String toString() {
    return "ParametreEntree [nom=" + nom + ", description=" + description
    + ", is Obligatoire=" + obligatoire + ", isModifiable=" + modification + ", type=" + type + "]";
  }
  
  public String declareConstante(){
    StringBuilder sb = new StringBuilder("");
    sb.append("private ").append(type).append(" ").append("m").append(nom).append(" = null;");
    return sb.toString();
  }
  
  public String declareComposantMetier(){
    StringBuilder sb = new StringBuilder("");
    if (getComposantMetier()!=null){
     if (getComposantMetier().getType().contains("Ref")){
       sb.append("private "+UpperFirst(getComposantMetier().getType())+" m"+getComposantMetier().getType().toUpperCase()+" =  new "+UpperFirst(getComposantMetier().getType())+"();\n");
     }else{
       sb.append("private "+UpperFirst(getComposantMetier().getType())+"Criterion m"+getComposantMetier().getType().toUpperCase()+" =  new "+UpperFirst(getComposantMetier().getType())+"Criterion();\n");
     }
    }
    
    return sb.toString();
  }
  
  public String declareGetterComposantMetier(){
    StringBuilder sb = new StringBuilder("");
    if (getComposantMetier()!=null){
      if (isObligatoire()){
        sb.append("@RequiredConstraint\n");
      }
     if (getComposantMetier().getType().contains("Ref")){
       sb.append("public "+UpperFirst(getComposantMetier().getType())+" get"+getComposantMetier().getType().toUpperCase()+"() {\n");
       sb.append("return m"+getComposantMetier().getType().toUpperCase()+";\n");
       sb.append("}\n");
     }else{
       sb.append("public "+UpperFirst(getComposantMetier().getType())+"Criterion get"+getComposantMetier().getType().toUpperCase()+"() {\n");
       sb.append("return m"+getComposantMetier().getType().toUpperCase()+";\n");
       sb.append("}\n");
     }
    }
    
    return sb.toString();
  }
  
  public String declareSetterComposantMetier(){
    StringBuilder sb = new StringBuilder("");
    if (getComposantMetier()!=null){
     if (getComposantMetier().getType().contains("Ref")){
       sb.append("public void set"+getComposantMetier().getType().toUpperCase()+"("+UpperFirst(getComposantMetier().getType())+" "+getComposantMetier().getType()+") {\n");
       sb.append("if (isLoggingDebug())\n");
       sb.append("    logDebug(\"set"+getComposantMetier().getType().toUpperCase()+"() <-- \" + "+getComposantMetier().getType()+");\n");
       sb.append("  m"+getComposantMetier().getType().toUpperCase()+" = "+getComposantMetier().getType()+";\n");
       sb.append("}\n");
     }else{
       sb.append("public void set"+getComposantMetier().getType().toUpperCase()+"("+UpperFirst(getComposantMetier().getType())+"Criterion "+getComposantMetier().getType()+") {\n");
       sb.append("if (isLoggingDebug())\n");
       sb.append("    logDebug(\"set"+getComposantMetier().getType().toUpperCase()+"() <-- \" + "+getComposantMetier().getType()+");\n");
       sb.append("  m"+getComposantMetier().getType().toUpperCase()+" = "+getComposantMetier().getType()+";\n");
       sb.append("}\n");
     }
    }
    
    return sb.toString();
  }
  
  public String declareGetterForBaseProxy(){    
    //StringBuilder sb = new StringBuilder(" \n /**\n   * @return la valeur positionn�e au sein de la propri�t�\n   */\n");
    StringBuilder sb = new StringBuilder("");
    sb.append(declareAnnotation());
    sb.append("public ").append(type).append(" get").append(nom).append("(){\n");
    sb.append("\t return m").append(nom).append(";\n");
    sb.append("}\n");
    return sb.toString();
  }
  
  public String declareSetterForBaseProxy(){    
    //StringBuilder sb = new StringBuilder("  /**\n   * @param ").append(nom).append(" valeur � positionner au sein de la propri�t�\n   */\n");
    StringBuilder sb = new StringBuilder("");
    sb.append("public void set").append(nom).append("(").append(type).append(" ").append(nom).append("){\n");
    sb.append("if (isLoggingDebug())\n");
    sb.append("\t logDebug(\" set").append(nom).append("() <-- \" + ").append(nom).append(");\n");
    sb.append("m").append(nom).append(" = ").append(nom).append(";\n");
    sb.append("}");
    return sb.toString();
  }
  
  public String declareParameters(){
    //StringBuilder sb = new StringBuilder("/** Nom du param�tre donnant acc�s au ").append(nom).append(" */\n");
    StringBuilder sb = new StringBuilder("");
    if (getComposantMetier()!=null && getComposantMetier().getType().contains("Ref")){
      sb.append("public static final String ").append(getComposantMetier().getType().toUpperCase()).append("_PARAMETER_NAME = \"").append(getComposantMetier().getType().toUpperCase()).append("\";\n");
    }else if (getComposantMetier()!=null){
      sb.append("public static final String ").append(getComposantMetier().getType().toUpperCase()).append("_PARAMETER_NAME = \"").append(getComposantMetier().getType().toUpperCase()).append("\";\n");
    }
    
    sb.append("public static final String ").append(nom).append("_PARAMETER_NAME = \"").append(nom).append("\";\n");
    return sb.toString();
  }
  
  
  
  public String declareCriteria(){
    //StringBuilder sb = new StringBuilder("/** Nom du critère donnant accès au ").append(nom).append(" */\n");
    StringBuilder sb = new StringBuilder("");
    sb.append("public static final String ").append(nom.toUpperCase()).append("_CRITERION_NAME = \"").append(nom).append("\";\n");
    if (this.getComposantMetier()!=null){
      sb.append("/** Nom du critère donnant accès au ").append(getComposantMetier().getType().toUpperCase()).append(" */\n");
      //@TODO si plusieur composant metier de meme type utiliser l'atttribut nomCriterionName
      sb.append("public static final String ").append(getComposantMetier().getType().toUpperCase()).append("_CRITERION_NAME = \"").append(getComposantMetier().getType().toUpperCase()).append("\";\n");
      if (getComposantMetier().getType().equals("paesi")){
        sb.append("public static final String INDPAESIINFERIEUR_CRITERION_NAME=\"INDPAESIInferieur\";\n");
      }
    }
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
    sb.append(declareAnnotation()).append("\n");
    sb.append("public ").append(type).append(" get").append(UpperFirst(nom)).append("(){\n");
    sb.append("\t return getCriterionValueAs").append(type).append("(").append(nom.toUpperCase()).append("_CRITERION_NAME);\n");
    sb.append("}\n");
    
    if (composantMetier!=null){
      sb.append("public ").append(composantMetier.getType().toUpperCase()+"Criterion").append(" get").append(composantMetier.getType().toUpperCase()).append("(){\n");
      sb.append("\t return ("+composantMetier.getType().toUpperCase()+"Criterion) getCriterionValue").append("(").append(getComposantMetier().getType().toUpperCase()).append("_CRITERION_NAME);\n");
      sb.append("}\n");
      if (getComposantMetier().getType().equals("paesi")){
        sb.append("public Boolean getINDPAESIInferieur(){\n");
        sb.append("  return (Boolean) getCriterionValue(INDPAESIINFERIEUR_CRITERION_NAME);\n");
        sb.append("}\n");
      }
    }
    return sb.toString();
  }
  
  public String declareGetterParameterForService(){
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
    sb.append(declareAnnotation()).append("\n");
    sb.append("public ").append(type).append(" get").append(UpperFirst(nom)).append("(){\n");
    sb.append("\t return getParameterValueAs").append(type).append("(").append(nom.toUpperCase()).append("_PARAMETER_NAME);\n");
    sb.append("}\n");
    
    if (composantMetier!=null){
      sb.append("public ").append(composantMetier.getType().toUpperCase()).append(" get").append(composantMetier.getType().toUpperCase()).append("(){\n");
      sb.append("\t return ("+composantMetier.getType().toUpperCase()+"Parameter) getParameterValue").append("(").append(getComposantMetier().getType().toUpperCase()).append("_PARAMETER_NAME);\n");
      sb.append("}\n");
      if (getComposantMetier().getType().equals("paesi")){
        sb.append("public Boolean getINDPAESIInferieur(){\n");
        sb.append("  return (Boolean) getParameterValue(INDPAESIINFERIEUR_CRITERION_NAME);\n");
        sb.append("}\n");
      }
    }
    return sb.toString();
  }
  public String declareAnnotation(){
    StringBuilder sb = new StringBuilder();
    if(type.equalsIgnoreCase("Integer")){
      if (getLongueur()!=null && getLongueur()>0){
        sb.append("  @ForAnInteger(useGrouping=false, digits="+getLongueur()+")\n");
      }else{
      sb.append("@ForAnInteger \n");
      }
    }else if(type.equalsIgnoreCase("Date")){
      sb.append("@ForADate \n");
    }else if(type.equalsIgnoreCase("String") && longueur!=null){
      sb.append("@MaxLengthConstraint(").append(longueur).append(")\n");
    }else if(type.equalsIgnoreCase("BigDecimal")){
      if (getLongueur()!=null && getLongueur()>0){
        sb.append("  @ForAnAmount(digits="+getLongueur()+",fractionDigit="+getLongueurFraction()+")\n");
      }else{
        sb.append("@ForADecimal(digits=").append(longueur).append(",fractionDigits=").append(longueurFraction).append(") \n");
      }
    }
    /*if (isObligatoire()){ //supprimer pour les serviceParameters et criteria
      sb.append("@RequiredConstraint\n");
    }*/
    return sb.toString();
  }
  /*
  public String checkCriterionIsEmpty(){
    StringBuilder sb = new StringBuilder();
    sb.append(" if(serviceCriteria.isCriterionEmpty(").append(tableName).append("BasePrimaryKeyServiceCriteria.").append(name).append("_CRITERION_NAME)||");
    sb.append(" serviceCriteria.hasCriterionWildCard(").append(tableName).append("BasePrimaryKeyServiceCriteria.").append(name).append("_CRITERION_NAME))");
    sb.append("{\n");
    sb.append("           addError(serviceResponse, new PropertyRequiredServiceError(").append(tableName).append("BasePrimaryKeyServiceCriteria.").append(name).append("_CRITERION_NAME));\n");
    sb.append("         }\n");
    return sb.toString();
  }*/
  
  /*
  public String checkParameterIsEmpty(){
    StringBuilder sb = new StringBuilder();
    sb.append(" if(serviceParameters.isParameterEmpty(").append(tableName).append("BaseServiceParameters.").append(name).append("_PARAMETER_NAME)){\n");
    sb.append("           addError(serviceResponse, new PropertyRequiredServiceError(\"Param�tre manquant: \",").append(tableName).append("BaseServiceParameters.").append(name).append("_PARAMETER_NAME));\n");
    sb.append("         }\n");
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
  
  private static String UpperFirst(String str){
    String result=null;
    String firstLetter=str.substring(0, 1).toUpperCase();
    result=firstLetter+str.substring(1, str.length());
    return result;
  }
}
