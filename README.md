# crapQL

A ideia dessa funcionalidade era criar uma maneira rápida de consultar nossas tabelas via url e receber um json em retorno, o nome crapQL vem dai! haha

O projeto inicial de consulta só continha, projection, sort, predicate, inclusão de distinct e paginação. O próximo passo seria incluir joins.

No fim a url ficava assim:

/*Nome da pasta*/*Nome da classe*?*parâmetros*

Na classe de teste dá de ver melhor como os parâmetros eram passados na URL, mas uns possíveis exemplos são:

/colaborador/Colaborador?projection=id,nome&nome=eq:carlos&idade=gte:35
