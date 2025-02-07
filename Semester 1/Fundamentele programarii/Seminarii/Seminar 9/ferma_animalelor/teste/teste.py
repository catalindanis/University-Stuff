from persistenta.repo_animale import FileRepoAnimale


class Teste:

    def ruleaza_toate_testele(self):
        file_repo_animale = FileRepoAnimale("teste/animale_test.txt")
        assert len(file_repo_animale)==7