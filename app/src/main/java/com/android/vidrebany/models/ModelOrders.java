package com.android.vidrebany.models;

public class ModelOrders {
    String code,
            adminUser,
            corteUser,
            canteadoUser,
            mecanizadoUser,
            lacaUser,
            espejosUser,
            cajonesUser,
            embalajeUser,
            uneroUser,
            transporteUser,
            montajeUser;

    long    adminStarted, adminEnded,
            uneroStarted, uneroEnded,
            espejosStarted, espejosEnded,
            cajonesStarted, cajonesEnded,
            corteStarted, corteEnded,
            canteadoStarted, canteadoEnded,
            mecanizadoStarted, mecanizadoEnded,
            lacaStarted, lacaEnded,
            montajeStarted, montajeEnded,
            embalajeStarted, embalajeEnded,
            transporteStarted, transporteEnded;

    boolean admin, montaje, corte, canteado, cajones, espejos, mecanizado, embalaje, transporte, laca, espejo, unero;

    public ModelOrders() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public String getCorteUser() {
        return corteUser;
    }

    public void setCorteUser(String corteUser) {
        this.corteUser = corteUser;
    }

    public String getCanteadoUser() {
        return canteadoUser;
    }

    public void setCanteadoUser(String canteadoUser) {
        this.canteadoUser = canteadoUser;
    }

    public String getMecanizadoUser() {
        return mecanizadoUser;
    }

    public void setMecanizadoUser(String mecanizadoUser) {
        this.mecanizadoUser = mecanizadoUser;
    }

    public String getLacaUser() {
        return lacaUser;
    }

    public void setLacaUser(String lacaUser) {
        this.lacaUser = lacaUser;
    }

    public String getEspejosUser() {
        return espejosUser;
    }

    public void setEspejosUser(String espejosUser) {
        this.espejosUser = espejosUser;
    }

    public String getCajonesUser() {
        return cajonesUser;
    }

    public void setCajonesUser(String cajonesUser) {
        this.cajonesUser = cajonesUser;
    }

    public String getEmbalajeUser() {
        return embalajeUser;
    }

    public void setEmbalajeUser(String embalajeUser) {
        this.embalajeUser = embalajeUser;
    }

    public String getUneroUser() {
        return uneroUser;
    }

    public void setUneroUser(String uneroUser) {
        this.uneroUser = uneroUser;
    }

    public String getTransporteUser() {
        return transporteUser;
    }

    public void setTransporteUser(String transporteUser) {
        this.transporteUser = transporteUser;
    }

    public String getMontajeUser() {
        return montajeUser;
    }

    public void setMontajeUser(String montajeUser) {
        this.montajeUser = montajeUser;
    }

    public long getAdminStarted() {
        return adminStarted;
    }

    public void setAdminStarted(long adminStarted) {
        this.adminStarted = adminStarted;
    }

    public long getAdminEnded() {
        return adminEnded;
    }

    public void setAdminEnded(long adminEnded) {
        this.adminEnded = adminEnded;
    }

    public long getUneroStarted() {
        return uneroStarted;
    }

    public void setUneroStarted(long uneroStarted) {
        this.uneroStarted = uneroStarted;
    }

    public long getUneroEnded() {
        return uneroEnded;
    }

    public void setUneroEnded(long uneroEnded) {
        this.uneroEnded = uneroEnded;
    }

    public long getEspejosStarted() {
        return espejosStarted;
    }

    public void setEspejosStarted(long espejosStarted) {
        this.espejosStarted = espejosStarted;
    }

    public long getEspejosEnded() {
        return espejosEnded;
    }

    public void setEspejosEnded(long espejosEnded) {
        this.espejosEnded = espejosEnded;
    }

    public long getCajonesStarted() {
        return cajonesStarted;
    }

    public void setCajonesStarted(long cajonesStarted) {
        this.cajonesStarted = cajonesStarted;
    }

    public long getCajonesEnded() {
        return cajonesEnded;
    }

    public void setCajonesEnded(long cajonesEnded) {
        this.cajonesEnded = cajonesEnded;
    }

    public long getCorteStarted() {
        return corteStarted;
    }

    public void setCorteStarted(long corteStarted) {
        this.corteStarted = corteStarted;
    }

    public long getCorteEnded() {
        return corteEnded;
    }

    public void setCorteEnded(long corteEnded) {
        this.corteEnded = corteEnded;
    }

    public long getCanteadoStarted() {
        return canteadoStarted;
    }

    public void setCanteadoStarted(long canteadoStarted) {
        this.canteadoStarted = canteadoStarted;
    }

    public long getCanteadoEnded() {
        return canteadoEnded;
    }

    public void setCanteadoEnded(long canteadoEnded) {
        this.canteadoEnded = canteadoEnded;
    }

    public long getMecanizadoStarted() {
        return mecanizadoStarted;
    }

    public void setMecanizadoStarted(long mecanizadoStarted) {
        this.mecanizadoStarted = mecanizadoStarted;
    }

    public long getMecanizadoEnded() {
        return mecanizadoEnded;
    }

    public void setMecanizadoEnded(long mecanizadoEnded) {
        this.mecanizadoEnded = mecanizadoEnded;
    }

    public long getLacaStarted() {
        return lacaStarted;
    }

    public void setLacaStarted(long lacaStarted) {
        this.lacaStarted = lacaStarted;
    }

    public long getLacaEnded() {
        return lacaEnded;
    }

    public void setLacaEnded(long lacaEnded) {
        this.lacaEnded = lacaEnded;
    }

    public long getMontajeStarted() {
        return montajeStarted;
    }

    public void setMontajeStarted(long montajeStarted) {
        this.montajeStarted = montajeStarted;
    }

    public long getMontajeEnded() {
        return montajeEnded;
    }

    public void setMontajeEnded(long montajeEnded) {
        this.montajeEnded = montajeEnded;
    }

    public long getEmbalajeStarted() {
        return embalajeStarted;
    }

    public void setEmbalajeStarted(long embalajeStarted) {
        this.embalajeStarted = embalajeStarted;
    }

    public long getEmbalajeEnded() {
        return embalajeEnded;
    }

    public void setEmbalajeEnded(long embalajeEnded) {
        this.embalajeEnded = embalajeEnded;
    }

    public long getTransporteStarted() {
        return transporteStarted;
    }

    public void setTransporteStarted(long transporteStarted) {
        this.transporteStarted = transporteStarted;
    }

    public long getTransporteEnded() {
        return transporteEnded;
    }

    public void setTransporteEnded(long transporteEnded) {
        this.transporteEnded = transporteEnded;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isMontaje() {
        return montaje;
    }

    public void setMontaje(boolean montaje) {
        this.montaje = montaje;
    }

    public boolean isCorte() {
        return corte;
    }

    public void setCorte(boolean corte) {
        this.corte = corte;
    }

    public boolean isCanteado() {
        return canteado;
    }

    public void setCanteado(boolean canteado) {
        this.canteado = canteado;
    }

    public boolean isCajones() {
        return cajones;
    }

    public void setCajones(boolean cajones) {
        this.cajones = cajones;
    }

    public boolean isEspejos() {
        return espejos;
    }

    public void setEspejos(boolean espejos) {
        this.espejos = espejos;
    }

    public boolean isMecanizado() {
        return mecanizado;
    }

    public void setMecanizado(boolean mecanizado) {
        this.mecanizado = mecanizado;
    }

    public boolean isEmbalaje() {
        return embalaje;
    }

    public void setEmbalaje(boolean embalaje) {
        this.embalaje = embalaje;
    }

    public boolean isTransporte() {
        return transporte;
    }

    public void setTransporte(boolean transporte) {
        this.transporte = transporte;
    }

    public boolean isLaca() {
        return laca;
    }

    public void setLaca(boolean laca) {
        this.laca = laca;
    }

    public boolean isEspejo() {
        return espejo;
    }

    public void setEspejo(boolean espejo) {
        this.espejo = espejo;
    }

    public boolean isUnero() {
        return unero;
    }

    public void setUnero(boolean unero) {
        this.unero = unero;
    }
}
