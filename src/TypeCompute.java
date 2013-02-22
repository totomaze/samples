import java.util.List;



/**
 * Paquet de définition
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
public class TypeCompute {
  
  /** Version extraite du gestionnaire de configuration */
  public static final String CLASS_VERSION = "$Revision:   1.0  $";
  
  private String code;//code
  private String type;//code REQ:de type requete SER:service facultatif COM:composantMetier
  private String typeComposantMetier;//obligataire si le type = COM
  private String requete;//obligataire si le type = REQ
  private String service;//obligataire si le type = SER
  private List<String> listeParametre;//facultatif , nom des champs a passer à la requete
  
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public String getRequete() {
    return requete;
  }
  public void setRequete(String requete) {
    this.requete = requete;
  }
  public List<String> getListeParametre() {
    return listeParametre;
  }
  public void setListeParametre(List<String> listeParametre) {
    this.listeParametre = listeParametre;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {

    this.service = service;
  }
  
  public String getTypeComposantMetier() {
    return typeComposantMetier;
  }

  public void setTypeComposantMetier(String typeComposantMetier) {
    this.typeComposantMetier = typeComposantMetier;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj != null){
      TypeCompute bloc= (TypeCompute) obj;
      if (this.getCode().equals(bloc.getCode())){
        return true;
      }else return false;
    }else{
      return super.equals(obj);
    }
      
    
  }
  
  



}
