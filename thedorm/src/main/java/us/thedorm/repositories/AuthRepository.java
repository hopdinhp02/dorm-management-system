package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import us.thedorm.models.UserInfo;

@Repository
@Transactional
public interface AuthRepository extends JpaRepository<UserInfo,Long>{
    @Query(value = "SELECT COUNT(*) as Total \n" +
            "                                 FROM [user_info] INNER JOIN [user_role] as [ur]\n" +
            "                                  ON [ur].[user_id] = [user_info].[id]\n" +
            "                                INNER JOIN [role]\n" +
            "                               ON [role].[id] = [ur].[role_id]\n" +
            "                               INNER JOIN [role_permission]\n" +
            "                                 ON [role_permission].[permission_id] = [role].[id]\n" +
            "                                 INNER JOIN [permission] ON [permission].[id] = [role_permission].[permission_id]\n" +
            "                 WHERE [user_info].[id] = ? \n" +
            "                 AND [permission].[title] = ? \n" +
            "                 AND [permission].[name] = ?",nativeQuery = true)
    int getNumberOfPermission(long id,String title,String name);
}
