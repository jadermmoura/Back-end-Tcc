package br.edu.ifrs.restinga.requisicoes.modelo;

public class Login {
    private String usuario;
    private String senha;

    public Login() {}

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}