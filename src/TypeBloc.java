
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
public class TypeBloc {
  
  /** Version extraite du gestionnaire de configuration */
  public static final String CLASS_VERSION = "$Revision:   1.0  $";
  
  private String code;//code du bloc
  private String libelle;//libelle facultatif
  private boolean reduitParDefaut=false;
  private String logicalName;
  private Integer nbChampParLigne;

  public boolean isReduitParDefaut() {
    return reduitParDefaut;
  }

  public void setReduitParDefaut(boolean reduitParDefaut) {
    this.reduitParDefaut = reduitParDefaut;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLibelle() {
    return libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }


  public String getLogicalName() {
    return logicalName;
  }

  public void setLogicalName(String logicalName) {
    this.logicalName = logicalName;
  }

  public Integer getNbChampParLigne() {
    return nbChampParLigne;
  }

  
  /**
   * @param nbChampParLigne valeur à positionner au sein de la propriété
   */
  public void setNbChampParLigne(Integer nbChampParLigne) {
    this.nbChampParLigne = nbChampParLigne;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj != null){
      TypeBloc bloc= (TypeBloc) obj;
      if (this.getCode().equals(bloc.getCode())){
        return true;
      }else return false;
    }else{
      return super.equals(obj);
    }
      
    
  }
  
  



}
