package ru.otus.work25.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.dao.AclRepository;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.MongoAcl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class PermissionService {

    private final AclRepository aclRepository;

    public boolean hasWritePermissions(Serializable id, String className) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> authorities = userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList());
        List<MongoAcl> acls = aclRepository.findByInstanceIdAndClassName(id, className);
        for(MongoAcl acl: acls) {
            boolean hasWritePermission = acl.getPermissions().stream().filter(permission ->
                    permission.getSid().getName().equals(userDetails.getUsername()) || authorities.contains(permission.getSid().getName())
            ).filter(permission -> permission.getPermission() == BasePermission.WRITE.getMask()).map(permission -> true).findFirst().isPresent();
            if(hasWritePermission) {
                return true;
            }
        }
        return false;
    }

    public boolean hasReadPermissions(Serializable id, String className) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> authorities = userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList());
        List<MongoAcl> acls = aclRepository.findByInstanceIdAndClassName(id, className);
        for(MongoAcl acl: acls) {
            boolean hasReadPermission = acl.getPermissions().stream().filter(permission ->
                    permission.getSid().getName().equals(userDetails.getUsername()) || authorities.contains(permission.getSid().getName())
            ).filter(permission -> permission.getPermission() == BasePermission.READ.getMask()).map(permission -> true).findFirst().isPresent();
            if(hasReadPermission) {
                return true;
            }
        }
        return false;
    }
}
