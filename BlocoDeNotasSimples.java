import javax.swing.*;
import java.awt.*; //BorderLayout, GridLayout estão aqui
import java.awt.event.*; //ActionListener, ActionEveent
import java.io.*; //File, Reader/Writer, Buffer

public class BlocoDeNotasSimples extends JFrame implements ActionListener {
    
    private JTextArea areaTexto;    //Campo grande para escrever
    private JButton btnSalvar, btnAbrir, btnLimpar, btnSalvarComo, btnAbrirEm;
    private boolean alterado = false;
    
    private final File arquivoPadrao = new File("texto.txt");
    
    public BlocoDeNotasSimples(){
        super ("Bloco de Notas Simples");
        
        //----------------------- 1) DEFINIR O LAYOUT DO FRAME ----------------
        //BorderLayout divide o espaço em regiões (NORTH, SOUTH, CENTER, EASTY, WEST).
        //Isso deixa a janela responsiva, sem precisar de setBounds.
        setLayout(new BorderLayout());
        
        //----------------------- 1) NORTH: TÌTULO ----------------
        JLabel titulo = new JLabel("Bloco de notas - BorderLayout + GridLayout + arquivos", SwingConstants.CENTER);
        //Adiciona no topo (NORTH). A altura do NORTH se ajusta ao conteúdo.
        add(titulo, BorderLayout.NORTH);
        
        //----------------------- 3) CENTER: ÁREA DE TEXTO COM ROLAGEM ----------------
        areaTexto = new JTextArea();
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        //JScrollPane fornece barras de rolagem ao redor do JTextArea
        JScrollPane scroll = new JScrollPane(areaTexto);
        //a região CENTER (centro da tela) ocupa todo o espaço restante
        add(scroll, BorderLayout.CENTER);
        
        //--------------------- 4) SOUTH: PAINEL COM BOTÕES EM GRADE (grid) ----------------
        //Usamos o JPanel com GridLayout para alinha os 3 botoes igualmente 
        //GridLayout (1, 3, 8, 8) => 1 Linha, 3 Colunas, Espaçamento de 8px.
        JPanel painelBotoes = new JPanel(new GridLayout(1, 3, 8, 8));
        btnSalvar = new JButton("Salvar");
        btnAbrir = new JButton("Abrir");
        btnLimpar = new JButton("Limpar");
        btnSalvarComo = new JButton("Salvar como...");
        btnAbrirEm = new JButton("Abrir em...");
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnAbrir);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnSalvarComo);
        painelBotoes.add(btnAbrirEm);
        //Adiciona o painel de botões no SOUTH (parte de baixo da janela)
        add(painelBotoes, BorderLayout.SOUTH);
        
        //--------------------- 5) REGISTRAR EVENTOS (LISTENERS) ----------------
        //Este frame implementa (implements) ActionListener, então "this" é o ouvinte
        btnSalvar.addActionListener(this);
        btnLimpar.addActionListener(this);
        btnAbrir.addActionListener(this);
        btnSalvarComo.addActionListener(this);
        btnAbrirEm.addActionListener(this);
        
        //--------------------- 6) CONFIGURAÇÕES FINAIS DA JENELA ----------------
        setSize(640, 480);                          //Tamanho Inicial
        setLocationRelativeTo(null);                //Centraliza a janela 
        setDefaultCloseOperation(EXIT_ON_CLOSE);    //Encerra a aplicação
        setVisible(true);                           //Torna a janela visível
        
    }
    
    //--------------------- TRATAMENTO DOS BOTÕES ----------------
    @Override
    public void actionPerformed(ActionEvent e) {
        Object origem = e.getSource(); //Identifica qual componente gerou o evento
        if(origem == btnSalvar) {
            salvarArquivo();
        }else if(origem == btnAbrir) {
            abrirArquivo();
        }else if(origem == btnLimpar) {
            limparTexto();
        }else if (origem == btnSalvarComo) {
            salvarComo();
        } else if (origem == btnAbrirEm) {
            abrirComChooser();
        }
    }
    
    //--------------------- MÉTODOS DE ARQUIVOS ----------------
    /*Salva o conteúdo da area de texto dentro do arquivo "texto.txt"
      try-cath-resources => o Java fechar o arquivo automaticamente ao final de bloco
    */
    private void salvarArquivo(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoPadrao))){
            //bw.write escreve/grava no arquivo
            //.getText() pega o texto do JTextArea
            bw.write(areaTexto.getText());
            
            //Menssagem amigável para o usuário com caminho completo
            JOptionPane.showMessageDialog(this, "Arquivo salvo em:\n" + arquivoPadrao.getAbsolutePath());
            //Feedback visual no título da janela
            setTitle("Bloco de Notas Simples - (Salvo)");
        }catch(IOException e){
            //Se algo der errado (prmissaão, disco cheio, caminho inválido, etc)
            //Tratamos aqui. MessageDialog exibe o erro para o usuário.
            JOptionPane.showMessageDialog(this, "Erro ao salvar:" + e.getMessage());
        }
    }
    //Lê todo o conteúdo do arquivo "texto.txt" e coloca na volta na area de texto
    //Também utilizza try-catch-resources para fechar o leitor automaticamente
    private void abrirArquivo(){
        //Primeiro, verificamos a existência do arquivo
        if(!arquivoPadrao.exists()){
            JOptionPane.showMessageDialog(this, "Arquivo não encontrado:\n" + arquivoPadrao.getAbsolutePath());
            return; //Não tenta abrir se não exixtir
        }
        
        StringBuilder conteudo = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(arquivoPadrao))){
            String linha;
            //Lemos Linha a linha até acabar (readiline() retorna null no fim)
            while((linha = br.readLine()) != null){
                conteudo.append(linha).append("\n");
            }
            //Mostra o conteúdo no JTextArea
            areaTexto.setText(conteudo.toString());
            setTitle("Bloco de Notas Simples - (Arquivo Aberto)");
            
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, "Erro ao abrir:" + e.getMessage());
        }
    }
    
    //Limpa o conteudo do JTextArea e restaura otitulo original
    private void limparTexto(){
        areaTexto.setText("");
        setTitle("Bloco de Notas Simples");
    }
    
     //Extra
    private void salvarComo() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File destino = chooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(destino))) {
                bw.write(areaTexto.getText());
                JOptionPane.showMessageDialog(this, "Salvo em:\n" + destino.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
            }
        }
    }

    private void abrirComChooser() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File origem = chooser.getSelectedFile();
            StringBuilder conteudo = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(origem))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    conteudo.append(linha).append("\n");
                }
                areaTexto.setText(conteudo.toString());
                setTitle("Bloco de Notas — " + origem.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir: " + e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        //Boas praticas: iniciar Swing na Event Dispatch Thread (EDT)
        //Por quê? Swing não é thread-safe, isso
        SwingUtilities.invokeLater(() -> new BlocoDeNotasSimples());
    }

}
